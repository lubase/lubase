package com.lubase.core.service.userright.impl;

import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.service.userright.RoleRightService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.UserRoleAssignService;
import com.lubase.core.service.userright.model.ColumnRightModelVO;
import com.lubase.core.service.userright.model.RoleModel;
import com.lubase.core.service.userright.model.UserRightInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRightServiceImpl implements UserRightService {

    @Autowired
    RoleRightService roleRightService;

    @Autowired
    UserRoleAssignService userRoleAssignService;

    //@CachePut(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_USER_RIGHT_INFO + "+#userId")
    @Override
    public UserRightInfo getUserRight(Long userId) {
        UserRightInfo userRightInfo = new UserRightInfo();
        userRightInfo.setUserId(userId);

        List<Long> userRoleList = userRoleAssignService.getUserRoleList(userId);
        userRightInfo.setRoleList(userRoleList);

        List<Long> funcList = new ArrayList<>();
        for (Long roleId : userRoleList) {
            funcList.addAll(roleRightService.getRoleFuncList(roleId));
        }
        userRightInfo.setFuncRightList(funcList);
        userRightInfo.setColRightList(getUserColumnRight(userRoleList));

        if (userRoleList.stream().anyMatch(r -> r.equals(CacheRightConstant.SUPPER_ADMINISTRATOR_ROLE_ID))) {
            userRightInfo.setIsSupperAdministrator(true);
        } else if (userRoleList.stream().anyMatch(r -> r.equals(CacheRightConstant.APP_ADMINISTRATOR_ROLE_ID)) ||
                userRoleList.stream().anyMatch(r -> r.equals(CacheRightConstant.APP_SETTING_ROLE_ID))) {
            userRightInfo.setIsAppAdministrator(true);
        }
        return userRightInfo;
    }

    /**
     * 获取用户的字段权限列表
     *
     * @param userRoleList
     * @return
     */
    List<ColumnRightModelVO> getUserColumnRight(List<Long> userRoleList) {
        List<ColumnRightModelVO> allRightList = new ArrayList<>();
        for (Long roleId : userRoleList) {
            List<ColumnRightModelVO> roleRightList = roleRightService.getRoleColList(roleId);
            for (ColumnRightModelVO vo : roleRightList) {
                ColumnRightModelVO existsRight = allRightList.stream().filter(r -> r.getColumnId().equals(vo.getColumnId())).findFirst().orElse(null);
                if (existsRight == null) {
                    allRightList.add(vo);
                } else {
                    existsRight.setAccessRight(Math.max(vo.getAccessRight(), existsRight.getAccessRight()));
                }
            }
        }
        return allRightList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_USER_APP + "+#userId")
    @Override
    public List<Long> getUserAllApp(Long userId) {
        List<Long> roleList = userRoleAssignService.getUserRoleList(userId);
        List<Long> appList = new ArrayList<>();
        for (Long roleId : roleList) {
            RoleModel roleModel = roleRightService.getRoleInfoById(roleId);
            if (roleModel != null && !appList.contains(roleModel.getAppId())) {
                appList.add(roleModel.getAppId());
            }
        }
        return appList;
    }

    @Override
    public Boolean checkFuncRight(UserRightInfo rightInfo, Long funcId) {
        if (rightInfo.getIsSupperAdministrator()) {
            return true;
        }
        if (rightInfo.getFuncRightList() != null && rightInfo.getFuncRightList().contains(funcId)) {
            return true;
        } else {
            return false;
        }
    }
}
