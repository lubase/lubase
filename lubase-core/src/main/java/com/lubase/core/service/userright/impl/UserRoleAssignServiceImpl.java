package com.lubase.core.service.userright.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.service.userright.UserRoleAssignService;
import com.lubase.core.service.userright.mapper.UserOrgRightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRoleAssignServiceImpl implements UserRoleAssignService {
    @Autowired
    DataAccess dataAccess;

    @Autowired
    UserOrgRightMapper userOrgRightMapper;

    @Override
    public List<Long> getUserAssignRoleList(Long userId) {
        QueryOption queryOption = new QueryOption("sa_role_assign");
        queryOption.setFixField("role_id");
        TableFilter filter = TableFilterWrapper.and().eq("account_id", userId).build();
        //.eq("role_id.enable_tag", "1").build();
        queryOption.setBuildLookupField(false);
        queryOption.setTableFilter(filter);
        List<Long> list = new ArrayList<>();
        List<DbEntity> entities = dataAccess.queryAllData(queryOption).getData();
        for (DbEntity entity : entities) {
            list.add(TypeConverterUtils.object2Long(entity.get("role_id")));
        }
        return list;
    }

    @Override
    public List<Long> getOrgAssignRoleList(Long userId) {
        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setFixField("organization_id");
        queryOption.setBuildLookupField(false);
        queryOption.setTableFilter(new TableFilter("id", userId, EOperateMode.Equals));
        DbCollection collUser = dataAccess.query(queryOption);
        Long orgId = 0L;
        if (collUser.getData().size() == 1 && collUser.getData().get(0).containsKey("organization_id")) {
            orgId = TypeConverterUtils.object2Long(collUser.getData().get(0).get("organization_id"), 0L);
        }
        return getOrgRoleList(orgId);
    }

    private List<Long> getOrgRoleList(Long orgId) {
        if (orgId == null || orgId == 0) {
            return new ArrayList<>();
        }
        List<Long> list = new ArrayList<>();
        List<String> entities = userOrgRightMapper.getRoleListByOrgId(orgId.toString());
        for (String roleId : entities) {
            list.add(TypeConverterUtils.object2Long(roleId));
        }
        return list;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_USER_ROLE + "+#userId")
    @Override
    public List<Long> getUserRoleList(Long userId) {
        List<Long> list = new ArrayList<>();
        //1、获取授予自己的角色
        list.addAll(getUserAssignRoleList(userId));

        //2、增加用户所属部门的角色
        list.addAll(getOrgAssignRoleList(userId));

        //3、增加各个应用下的公开角色
        QueryOption queryOption = new QueryOption("sa_role");
        queryOption.setBuildLookupField(false);
        queryOption.setFixField("id,role_type,public_role,app_id");
        TableFilterWrapper filterWrapper = TableFilterWrapper.or();
        for (Long roleId : list) {
            filterWrapper.eq("id", roleId);
        }
        filterWrapper.eq("public_role", 1);
        TableFilter filter = filterWrapper.build();
        queryOption.setTableFilter(filter);
        DbCollection collPublicRole = dataAccess.queryAllData(queryOption);

        List<Long> allRoleList = new ArrayList<>();
        // publicRole=2 排他角色，每个应用下只能有一个
        Map<String, Long> lockRoleMap = new HashMap<>();
        for (DbEntity entity : collPublicRole.getData()) {
            Integer publicRole = TypeConverterUtils.object2Integer(entity.get("public_role"));
            if (publicRole.equals(2)) {
                String appId = entity.get("app_id").toString();
                if (!lockRoleMap.containsKey(appId)) {
                    lockRoleMap.put(appId, TypeConverterUtils.object2Long(entity.getId()));
                }
            }
        }
        for (DbEntity entity : collPublicRole.getData()) {

            String appId = entity.get("app_id").toString();
            if (lockRoleMap.containsKey(appId)) {
                continue;
            }
            allRoleList.add(entity.getId());
        }
        for (String key : lockRoleMap.keySet()) {
            allRoleList.add(lockRoleMap.get(key));
        }
        return allRoleList;
    }
}
