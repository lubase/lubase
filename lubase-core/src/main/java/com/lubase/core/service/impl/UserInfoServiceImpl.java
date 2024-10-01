package com.lubase.core.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lubase.core.constant.CommonConstant;
import com.lubase.core.exception.LoginErrorException;
import com.lubase.core.extend.UserCreateExtendService;
import com.lubase.core.extend.UserLoginExtendService;
import com.lubase.core.extend.service.UserInfoExtendServiceAdapter;
import com.lubase.core.model.LoginInfoModel;
import com.lubase.core.model.NavVO;
import com.lubase.core.model.SelectUserModel;
import com.lubase.core.model.UserInfoModel;
import com.lubase.core.service.AppNavDataService;
import com.lubase.core.service.UserInfoService;
import com.lubase.core.service.VerifyCodeService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.model.UserRightInfo;
import com.lubase.core.util.StringEncodeUtil;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.model.ResourceDataModel;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author A
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Value("${lubase.password-reg:^(?![a-zA-Z]+$)(?![a-z0-9]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9~!@#$%^&*()\\-_+?/\\.]{8,16}$}")
    private String passwordReg;
    @Value("${lubase.password-tip:密码不符合复杂度(需包括数字、小写字母、大写字母、特殊字符四种的三种，长度在8-16位)}")
    private String passwordTip;
    /**
     * 配置的默认密码
     */
    @Value("${lubase.default-password:12345.com}")
    private String defaultPwd;
    @Autowired
    DataAccess dataAccess;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    UserRightService userRightService;

    @Autowired
    UserInfoExtendServiceAdapter userInfoExtendServiceAdapter;

    @Autowired
    AppNavDataService appNavDataService;
    @Autowired
    VerifyCodeService verifyCodeService;
    /**
     * jwt 默认的secret
     */
    public static String secretKey;

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Value("${lubase.jwt-secret:abcdef}")
    public void setSecretKey(String preSecretKey) {
        if (StringUtils.isAllEmpty(preSecretKey)) {
            secretKey = CommonConstant.JWT_SECRET_KEY;
        } else {
            secretKey = preSecretKey + CommonConstant.JWT_SECRET_KEY;
        }
    }

    @Override
    public UserInfoModel getUserInfo(String userCode) {
        if (StringUtils.isEmpty(userCode)) {
            return null;
        }
        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setTableFilter(new TableFilter("user_code", userCode));
        queryOption.setFixField("id,user_code,user_name,enable_tag");
        DbCollection coll = dataAccess.queryAllData(queryOption);
        if (!coll.getData().isEmpty()) {
            DbEntity entity = coll.getData().get(0);
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setId(entity.getId());
            userInfoModel.setUserCode(entity.get("user_code").toString());
            userInfoModel.setUserName(entity.get("user_name").toString());
            userInfoModel.setEnableTag(TypeConverterUtils.object2Boolean(entity.get("enable_tag")));
            return userInfoModel;
        }
        return null;
    }

    @SneakyThrows
    @Override
    public LoginInfoModel userLogin(String uid, String pwd, String vcode) throws LoginErrorException {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(pwd)) {
            throw new WarnCommonException("用户名或密码不能为空");
        }
        LoginInfoModel infoModel = verifyCodeService.checkVerifyCode(vcode, uid);
        if (infoModel.getLoginUser().getErrorCount() < 0) {
            return infoModel;
        }
        // 根据用户名和密码获取登录信息
        UserLoginExtendService userLoginExtendService = userInfoExtendServiceAdapter.getUserLoginExtendService();
        if (userLoginExtendService != null) {
            infoModel = userLoginExtendService.userLogin(uid, pwd);
        } else {
            infoModel = getUserInfoInSystem(uid, pwd);
        }
        //登录失败时处理逻辑
        if (infoModel == null || infoModel.getLoginUser() == null) {
            if (infoModel == null) {
                infoModel = new LoginInfoModel();
            }
            if (infoModel.getLoginUser() == null) {
                infoModel.setLoginUser(new LoginUser());
            }
            var count = verifyCodeService.calcErrorCount(uid);
            log.info(String.format("%s 登录失败%s次", uid, count));
            infoModel.getLoginUser().setErrorCount(count);
            if (infoModel.getLoginErrorException() == null) {
                if (count == 5) {
                    //表示下次需要验证码了
                    infoModel.setLoginErrorException(new LoginErrorException("420", "用户名或密码错误"));
                } else {
                    infoModel.setLoginErrorException(new LoginErrorException("401", "用户名或密码错误"));
                }
            }
            return infoModel;
        }

        var user = infoModel.getLoginUser();
        if (user != null && user.getId() != null && user.getId() > 0L) {
            userRightService.getUserRight(user.getId());
            user.setToken(createUserToken(user));
            //TODO 清缓存失败了怎么办？
            verifyCodeService.clearCacheErrorCount(uid);
        }
        return infoModel;
    }

    @SneakyThrows
    @Override
    public List<SelectUserModel> createUser(List<SelectUserModel> list) {
        // 参数检查
        List<SelectUserModel> data = new ArrayList<>();
        for (SelectUserModel user : list) {
            if (user.getId() == null || user.getId().isEmpty() || user.getUserCode() == null || user.getUserCode().isEmpty() || user.getUserName() == null || user.getUserName().isEmpty() || user.getDeptId() == null || user.getDeptId().isEmpty() || user.getDeptName() == null || user.getDeptName().isEmpty()) {
                throw new WarnCommonException("参数传递错误");
            }
            if (data.stream().anyMatch(x -> x.getId().equals(user.getId()))) {
                continue;
            }
            data.add(user);
        }
        if (data.isEmpty()) {
            throw new WarnCommonException("参数传递错误");
        }
        // 如果扩展了用户创建服务则使用扩展的服务
        UserCreateExtendService userCreateExtendService = userInfoExtendServiceAdapter.getUserCreateExtendService();
        if (userCreateExtendService != null) {
            return userCreateExtendService.createUser(data);
        }

        QueryOption queryOption = new QueryOption("sa_account");
        TableFilterWrapper filterWrapper = TableFilterWrapper.or();
        for (SelectUserModel user : data) {
            filterWrapper.eq("user_code", user.getUserCode());
        }
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collExistsUser = dataAccess.queryAllData(queryOption);
        if (collExistsUser.getData().size() == data.size()) {
            // 用户已经都存在返回
            for (SelectUserModel user : list) {
                collExistsUser.getData().stream().filter(d -> d.get("user_code").toString().equals(user.getUserCode())).findFirst().ifPresent(dbUser -> user.setId(dbUser.getId().toString()));
            }
            return list;
        }

        queryOption = new QueryOption("sa_organization");
        filterWrapper = TableFilterWrapper.or();
        for (SelectUserModel user : data) {
            filterWrapper.eq("id", user.getDeptId());
        }
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collExistsDept = dataAccess.queryAllData(queryOption);

        // 创建用户信息
        for (SelectUserModel user : data) {
            if (collExistsUser.getData().stream().anyMatch(x -> x.get("user_code").equals(user.getUserCode()))) {
                continue;
            }
            DbEntity entityUser = collExistsUser.newEntity();
            entityUser.put("organization_id", user.getDeptId());
            entityUser.put("user_code", user.getUserCode());
            entityUser.put("user_name", user.getUserName());
            entityUser.put("password", StringEncodeUtil.strToMd5Str(defaultPwd));
            entityUser.put("enable_tag", 1);
            collExistsUser.getData().add(entityUser);

            if (collExistsDept.getData().stream().noneMatch(x -> x.get("id").toString().equals(user.getDeptId()))) {
                // 不存在的部门默认创建到根节点下面，并且用deptId作为部门id进行使用
                DbEntity entityDept = collExistsDept.newEntity();
                entityDept.setId(Long.parseLong(user.getDeptId()));
                entityDept.put("org_name", user.getDeptName());
                entityDept.put("parent_id", StringUtils.isEmpty(user.getParentDeptId()) ? 0 : Long.parseLong(user.getParentDeptId()));
                collExistsDept.getData().add(entityDept);
            }
        }
        dataAccess.update(collExistsDept);
        dataAccess.update(collExistsUser);
        // 用户已经都存在返回
        for (SelectUserModel user : list) {
            collExistsUser.getData().stream().filter(d -> d.get("user_code").toString().equals(user.getUserCode())).findFirst().ifPresent(dbUser -> user.setId(dbUser.getId().toString()));
        }
        return list;
    }

    private LoginInfoModel getUserInfoInSystem(String userId, String pwd) {
        LoginInfoModel infoModel = new LoginInfoModel();
        String encodePwd = StringEncodeUtil.strToMd5Str(pwd);
        QueryOption queryOption = new QueryOption("sa_account");
        TableFilter filter = TableFilterWrapper.and().eq("user_code", userId).eq("password", encodePwd).build();
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            Integer enableTag = TypeConverterUtils.object2Integer(entity.get("enable_tag"), 0);
            if (enableTag.equals(0)) {
                infoModel.setLoginErrorException(new LoginErrorException("402", "账号已经被禁用"));
                return infoModel;
            }
            LoginUser user = null;
            user = new LoginUser();
            user.setId(entity.getId());
            user.setCode(entity.get("user_code").toString());
            user.setName(entity.get("user_name").toString());
            user.setOrgId(TypeConverterUtils.object2String(entity.get("organization_id"), "0"));
            infoModel.setLoginUser(user);
        }
        return infoModel;
    }

    @Override
    public String validatePassword(String password) {
        var pattern = Pattern.compile(passwordReg);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return passwordTip;
        } else {
            return "";
        }
    }

    @SneakyThrows
    @Override
    public Integer modifyUserPwd(String uid, String newPwd, String oldPwd) {
        String encodePwd = StringEncodeUtil.strToMd5Str(newPwd);
        String encodeOldPwd = null;
        if (oldPwd != null) {
            encodeOldPwd = StringEncodeUtil.strToMd5Str(oldPwd);
        }
        QueryOption queryOption = new QueryOption("sa_account");
        TableFilter filter = TableFilterWrapper.and().eq("user_code", uid).build();
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            var originalPwd = entity.get("password");
            if (encodeOldPwd != null && !encodeOldPwd.equals(originalPwd)) {
                return -10;
            }
            entity.setState(EDBEntityState.Modified);
            entity.put("password", encodePwd);
            return dataAccess.update(collection);
        }
        return 0;
    }

    @Override
    public String createUserToken(LoginUser user) {
        // 设置token 有效期，8小时
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(8);
        String token = JWT.create().withAudience(user.getId().toString())
                .withClaim("uco", user.getCode())
                .withClaim("una", user.getName())
                .withClaim("uor", user.getOrgId())
                .withExpiresAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secretKey));
        return token;
    }


    @Override
    public LoginUser verifyToken(String token) throws AuthenticationException {
        Long userId = null;
        String code, name, orgId;
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            userId = Long.valueOf(decodedJWT.getAudience().get(0));
            code = decodedJWT.getClaim("uco").asString();
            name = decodedJWT.getClaim("una").asString();
            orgId = decodedJWT.getClaim("uor").asString();
        } catch (JWTDecodeException exception) {
            throw new AuthenticationException("401 payload");
        } catch (Exception exception) {
            throw new AuthenticationException("401 payload 2");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthenticationException("401 Signature");
        } catch (Exception exception) {
            throw new AuthenticationException("401 Signature 2");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setId(userId);
        loginUser.setName(name);
        loginUser.setCode(code);
        loginUser.setOrgId(orgId);
        loginUser.setToken(token);

        return loginUser;
    }

    @Override
    public List<NavVO> getAdminNavData() {
        List<NavVO> allNavVOList = appNavDataService.getAdminNavData();

        List<NavVO> rightNavList = new ArrayList<>();
        LoginUser user = appHolderService.getUser();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        if (rightInfo.getIsSupperAdministrator()) {
            rightNavList = allNavVOList;
        } else if (rightInfo.getIsAppAdministrator()) {
            for (NavVO nav : allNavVOList) {
                if (userRightService.checkFuncRight(rightInfo, nav.getId())) {
                    rightNavList.add(nav);
                }
            }
        }
        processCurrentLanguage(rightNavList, CommonConstant.SYSTEM_APP_ID.toString());
        return rightNavList;
    }

    private void processCurrentLanguage(List<NavVO> navList, String appId) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            return;
        }
        List<ResourceDataModel> resourceList = registerColumnInfoService.getResourceList(appId);
        if (resourceList == null || resourceList.isEmpty()) {
            return;
        }
        navList.parallelStream().forEach(field -> {
            ResourceDataModel resourceDataModel = resourceList.stream().filter(x -> x.getDataId().equals(field.getId())).findFirst().orElse(null);
            if (resourceDataModel != null) {
                field.setName(resourceDataModel.getMsg());
            }
        });
    }

    @SneakyThrows
    @Override
    public List<NavVO> getSettingNavData(Long appId) {
        if (appId == null) {
            throw new ParameterNotFoundException("appId");
        }
        //应用配置需要管理员才可以进行配置，其他人无权进行配置
        LoginUser user = appHolderService.getUser();
        if (!appNavDataService.getAppManager(appId).contains(user.getId().toString())) {
            return new ArrayList<>();
        }

        List<NavVO> allNavVOList = appNavDataService.getSettingNavData();
        List<NavVO> rightNavList = new ArrayList<>();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        if (rightInfo.getIsSupperAdministrator()) {
            for (NavVO nav : allNavVOList) {
                if (!CommonConstant.SUPPER_UNACCESS_PAGE.contains(nav.getId().toString())) {
                    rightNavList.add(nav);
                }
            }
        } else if (rightInfo.getIsAppAdministrator()) {
            for (NavVO nav : allNavVOList) {
                if (userRightService.checkFuncRight(rightInfo, nav.getId())) {
                    rightNavList.add(nav);
                }
            }
        }
        processCurrentLanguage(rightNavList, appId.toString());
        return rightNavList;
    }

    @SneakyThrows
    @Override
    public List<NavVO> getAppPreviewNavDataId(Long appId) {
        if (appId == null) {
            throw new ParameterNotFoundException("appId");
        }
        List<NavVO> allNavVOList = appNavDataService.getNavDataByAppId(appId);

        LoginUser user = appHolderService.getUser();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        //超级管理员无应用的具体访问权限，子管理员对自己的应用具有全部访问权限
        if (rightInfo.getIsAppAdministrator() && appNavDataService.getAppManager(appId).contains(user.getId().toString())) {
            processCurrentLanguage(allNavVOList, appId.toString());
            return allNavVOList;
        } else {
            return new ArrayList<>();
        }
    }


}
