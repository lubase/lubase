package com.lubase.core.service.impl;

import com.lubase.core.extend.UserSelectForComponentDataService;
import com.lubase.core.extend.service.UserInfoExtendServiceAdapter;
import com.lubase.core.model.SelectUserModel;
import com.lubase.core.model.UserSelectCollection;
import com.lubase.core.service.RenderCommonComponentService;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RenderCommonComponentServiceImpl implements RenderCommonComponentService {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    UserInfoExtendServiceAdapter userInfoExtendServiceAdapter;

    @SneakyThrows
    @Override
    public UserSelectCollection selectUserList(String userCode, String userName, Integer pageIndex, Integer pageSize, Boolean isSystemUser) {
        // 检查参数是否为空
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        UserSelectCollection userSelectCollection = new UserSelectCollection();

        /**
         * 获取外部扩展的数据源服务
         */
        UserSelectForComponentDataService userSelectForComponentDataService = userInfoExtendServiceAdapter.getComponentDataForSelectUserService();
        // 系统外用户 并且实现了相关服务
        if (!isSystemUser) {
            if (userSelectForComponentDataService != null) {
                return userSelectForComponentDataService.selectUserList(userCode, userName, pageIndex, pageSize);
            }
            else {
                throw new WarnCommonException("未实现弹窗选人外部数据源服务");
            }
        }

        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setFixField("user_code,user_name,organization_id");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        if (!StringUtils.isEmpty(userCode)) {
            filterWrapper.likeAll("user_code", userCode);
        }
        if (!StringUtils.isEmpty(userName)) {
            filterWrapper.likeAll("user_name", userName);
        }
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setPageIndex(pageIndex);
        queryOption.setPageSize(pageSize);
        DbCollection coll = dataAccess.query(queryOption);
        List<SelectUserModel> userList = new ArrayList<>();
        for (DbEntity entity : coll.getData()) {
            SelectUserModel userModel = new SelectUserModel();
            userModel.setId(entity.getId().toString());
            userModel.setUserCode(entity.get("user_code").toString());
            userModel.setUserName(entity.get("user_name").toString());
            userModel.setDeptId(entity.get("organization_id").toString());
            if (entity.getRefData() != null && entity.getRefData().containsKey("organization_idNAME")) {
                userModel.setDeptName(entity.getRefData().get("organization_idNAME"));
            }
            userList.add(userModel);
        }
        userSelectCollection.setData(userList);
        userSelectCollection.setTotalCount(coll.getTotalCount());
        return userSelectCollection;
    }


}
