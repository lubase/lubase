package com.lubase.core.service.impl;

import com.lubase.core.extend.ComponentDataForSelectUserService;
import com.lubase.core.extend.service.ComponentDataServiceAdapter;
import com.lubase.core.model.SelectUserModel;
import com.lubase.core.service.RenderCommonComponentService;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
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
    ComponentDataServiceAdapter componentDataServiceAdapter;

    @Override
    public List<SelectUserModel> selectUserList(String userCode, String userName, Integer pageIndex, Integer pageSize, Boolean isSystemUser) {
        // 检查参数是否为空
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }

        List<SelectUserModel> userList = new ArrayList<>();
        /**
         * 获取外部扩展的数据源服务
         */
        ComponentDataForSelectUserService componentDataForSelectUserService = componentDataServiceAdapter.getComponentDataForSelectUserService();
        // 系统外用户 并且实现了相关服务
        if (!isSystemUser && componentDataForSelectUserService != null) {
            return componentDataForSelectUserService.selectUserList(userCode, userName, pageIndex, pageSize);
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
        if (!isSystemUser) {
            // 为了模拟下 没实现外部数据源接口的数据
            filterWrapper.ne("user_code", "admin");
        }
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setPageIndex(pageIndex);
        queryOption.setPageSize(pageSize);
        DbCollection coll = dataAccess.query(queryOption);
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
        return userList;
    }


}
