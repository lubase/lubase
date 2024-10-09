package com.lubase.core.service.userright.impl;

import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.service.userright.UserRoleAssignService;
import com.lubase.core.service.userright.mapper.UserRightMapper;
import com.lubase.model.DbEntity;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.util.TypeConverterUtils;
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
    UserRightMapper userRightMapper;


    @Override
    public List<Long> getUserAssignRoleList(Long userId) {
        List<Long> list = new ArrayList<>();
        if (userId == null) {
            return list;
        }
        DBContextHolder.setMainDataSourceCode();
        List<DbEntity> entities = userRightMapper.getUserAssignRoleList(userId);
        for (DbEntity entity : entities) {
            list.add(TypeConverterUtils.object2Long(entity.get("role_id")));
        }
        return list;
    }

    @Override
    public List<Long> getOrgAssignRoleList(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        DBContextHolder.setMainDataSourceCode();
        Long orgId = userRightMapper.getUserOrgId(userId);
        if (orgId == null || orgId == 0) {
            return new ArrayList<>();
        }
        List<Long> list = new ArrayList<>();
        DBContextHolder.setMainDataSourceCode();
        List<String> entities = userRightMapper.getRoleListByOrgId(orgId.toString());
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
        // 将list 转为字符串，中间用逗号隔开
        StringBuilder ids = new StringBuilder("0");
        for (Long roleId : list) {
            ids.append(",").append(roleId);
        }
        DBContextHolder.setMainDataSourceCode();
        List<DbEntity> roleList=userRightMapper.getUserRoleList(ids.toString());
        List<Long> allRoleList = new ArrayList<>();
        // publicRole=2 排他角色，每个应用下只能有一个
        Map<String, Long> lockRoleMap = new HashMap<>();
        for (DbEntity entity : roleList) {
            Integer publicRole = TypeConverterUtils.object2Integer(entity.get("public_role"));
            if (publicRole.equals(2)) {
                String appId = entity.get("app_id").toString();
                if (!lockRoleMap.containsKey(appId)) {
                    lockRoleMap.put(appId, TypeConverterUtils.object2Long(entity.getId()));
                }
            }
        }
        for (DbEntity entity : roleList) {
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
