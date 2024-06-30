package com.lubase.sso.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.lubase.core.aop.LoginInfoVO;
import com.lubase.core.entity.SaOrganizationEntity;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.util.StringEncodeUtil;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.IDGenerator;
import com.lubase.orm.util.QueryOptionWrapper;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.sso.config.SSOConfig;
import com.lubase.sso.model.OAUserVo;
import com.lubase.sso.model.SSODataBo;
import com.lubase.sso.model.SSOResultDataBo;
import com.lubase.sso.service.ISSOUserService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SSOUserServiceImpl implements ISSOUserService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    SSOConfig ssoConfig;
    @Autowired(required = true)
    DataAccess dataAccess;
    @Autowired
    UserRightService userRightService;

    @Autowired
    IDGenerator idGenerator;

    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @SneakyThrows
    @Override
    public LoginUser getSSOUser(LoginInfoVO loginInfo) {
        String result = "";
        BufferedReader in = null;
        LoginUser user = null;
        RestTemplate restTemplate = new RestTemplate();
        String urlNameString = String.format("%s/authenticate/check_token?access_token=%s&platform_code=%s", ssoConfig.getSsoTokenUrl(),
                loginInfo.getPwd(), ssoConfig.getPlatformCode());
        SSODataBo ssoDataBo = null;
        try {
            ssoDataBo = restTemplate.getForObject(urlNameString, SSODataBo.class);
            log.info("校验token返回值：" + JSONObject.toJSONString(ssoDataBo));
        } catch (Exception e) {
            log.error("请求SSO认证中心校验token异常" + urlNameString, e);
        }

        if ("S_0000".equals(ssoDataBo.getKey())) {
            //根据usercode获取用户信息
            QueryOption queryOption = new QueryOption("sa_account");
            //默认账号不会重复
            TableFilter filter = TableFilterWrapper.and().eq("user_code", ssoDataBo.getResult().getUser_code()).build();
            queryOption.setTableFilter(filter);
            DbCollection collection = dataAccess.query(queryOption);
            if (collection.getData().size() == 1) {
                DbEntity entity = (DbEntity) collection.getData().get(0);
                Integer enableTag = TypeConverterUtils.object2Integer(entity.get("enable_tag"), 0);
                if (enableTag.equals(0)) {
                    throw new InvokeCommonException(String.format("账号已经被禁用，请联系管理员 %s", loginInfo.getUserCode()));
                }
                user = new LoginUser();
                user.setId(entity.getId());
                user.setCode(entity.get("user_code").toString());
                user.setName(entity.get("user_name").toString());
                this.userRightService.getUserRight(user.getId());
            } else {

                if (ssoDataBo.getResult() == null) {
                    log.error("请求SSO认证中心校验token异常" + urlNameString);
                    throw new InvokeCommonException(String.format("SSO 账号数据错误 %s", loginInfo.getUserCode()));
                } else { //如果账户信息没有此账户，需要创建用户与部门
                    user = createNewUserOrOrg(ssoDataBo.getResult());
                    this.userRightService.getUserRight(user.getId());
                }
            }
        } else {
            log.error("请求SSO认证中心校验token异常" + urlNameString);
            throw new InvokeCommonException(String.format("SSO 没有系统访问权限 %s", loginInfo.getUserCode()));
        }
        return user;
    }

    @SneakyThrows
    private LoginUser createNewUserOrOrg(SSOResultDataBo result) {
        DbCollection collEmptyOrg = dataAccess.getEmptyData(SaOrganizationEntity.TABLE_CODE);
        List<OAUserVo> oaUsers = new ArrayList<>();
        //组织实体组装
        //部门名称
        var orgName = TypeConverterUtils.object2String(result.getGroup_name());
        var groupId = TypeConverterUtils.object2String(result.getGroup_id());
        DbCollection collTopLevelOrg = getTopLevelOrg(orgName);
        var hasOrg = 0; //是否有部门，根据名称检索
        var orgId = 0L;
        var hasNull = 0;//groupid是否为空，根据名称检索
        if (collTopLevelOrg.getTotalCount() > 0) {
            hasOrg = 1;
            List<DbEntity> liGroup = collTopLevelOrg.getData().stream().filter(m -> !groupId.equals(m.get("group_id"))).collect(Collectors.toList());
            if (liGroup.size() > 0) {
                //只更新一个
                collTopLevelOrg.getData().get(0).setState(EDBEntityState.Modified);
                collTopLevelOrg.getData().get(0).put("group_id", groupId);
                hasNull = 1;
            }
            orgId = collTopLevelOrg.getData().get(0).getId();
        } else {
            orgId = idGenerator.nextId();
        }
        //部门为空
        if (hasOrg == 0) {
            //不存在 添加
            DbEntity entityOrg = collEmptyOrg.newEntity();
            entityOrg.setState(EDBEntityState.Added);
            //TODO 后期确定后再改 建到那级.
            entityOrg.setId(orgId);
            entityOrg.put("org_name", orgName);
            entityOrg.put("parent_id", 0);// 默认建到顶级
            entityOrg.put("group_id", groupId);
            collEmptyOrg.getData().add(entityOrg);
        }
        //用户实体组装
        //用户code
        var userCode = result.getUser_code();
        //用户名称
        var userName = result.getUser_name();
        DbCollection collEmptyUser = getCollEmptyUser();
        DbCollection collExistUser = getUsersBycode(userCode);
        var user = new LoginUser();
        if (collExistUser.getTotalCount() == 1) {
            user.setId(collExistUser.getData().get(0).getId());
            user.setCode(collExistUser.getData().get(0).get("user_code").toString());
            user.setName(collExistUser.getData().get(0).get("user_name").toString());
        } else {
            DbEntity entityUser = collEmptyUser.newEntity();
            entityUser.setId(idGenerator.nextId());
            entityUser.put("organization_id", orgId);
            entityUser.put("user_code", userCode);
            entityUser.put("user_name", userName);
            entityUser.put("password", StringEncodeUtil.strToMd5Str("888888"));
            collEmptyUser.getData().add(entityUser);

            user.setId(entityUser.getId());
            user.setCode(userCode);
            user.setName(userName);
        }
        changeDataSourceService.changeDataSourceByTableCode(SaOrganizationEntity.TABLE_CODE);
        //开启事务
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        var iRet = 0;
        try {
            if (!collEmptyOrg.getData().isEmpty()) {
                dataAccess.update(collEmptyOrg);
            }
            if (!collEmptyUser.getData().isEmpty()) {
                dataAccess.update(collEmptyUser);
            }
            if (hasNull == 1) {
                dataAccess.update(collTopLevelOrg);
            }
            transactionManager.commit(transactionStatus);
        } catch (Exception ex) {
            log.error("创建用户异常：" + result.getUser_code() + "," + ex.getMessage(), ex);
            transactionManager.rollback(transactionStatus);
            throw new InvokeCommonException("创建用户异常：" + result.getUser_code() + "," + ex.getMessage());
        }
        return user;
    }

    private DbCollection getTopLevelOrg(String orgName) {
        //顶级部门信息
        TableFilterWrapper wrapperOrg = new TableFilterWrapper(true);
        wrapperOrg.gt("id", 0).eq("org_name", orgName);
        QueryOption qoOrg = QueryOptionWrapper.select("id,org_name,group_id").from("sa_organization")
                .where(wrapperOrg.build()).build();
        DbCollection collTopLevelOrg = dataAccess.queryAllData(qoOrg);
        return collTopLevelOrg;
    }

    private DbCollection getUsersBycode(String userCode) {
        // 检索所有的工号
        TableFilterWrapper wrapperAll = new TableFilterWrapper(true);
        wrapperAll.eq("user_code", userCode);
        QueryOption qoAllUser = QueryOptionWrapper.select("id,user_code,user_name,phone").from("sa_account")
                .where(wrapperAll.build()).build();
        DbCollection dbCollectionAllUser = dataAccess.queryAllData(qoAllUser);
        return dbCollectionAllUser;
    }

    private DbCollection getCollEmptyUser() {
        //生成人员的空集合
        TableFilterWrapper wrapper = new TableFilterWrapper(true);
        wrapper.eq("id", -1);
        QueryOption qoUser = QueryOptionWrapper.select("id,user_code,user_name,password,organization_id,phone").from("sa_account")
                .where(wrapper.build()).build();
        DbCollection dbCollectionUser = dataAccess.query(qoUser);
        return dbCollectionUser;
    }
}
