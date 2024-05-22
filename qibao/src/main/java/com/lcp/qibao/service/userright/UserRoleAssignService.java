package com.lcp.qibao.service.userright;

import java.util.List;

public interface UserRoleAssignService {
    /**
     * 获取用户被授予的角色列表
     *
     * @param userId
     * @return
     */
    List<Long> getUserAssignRoleList(Long userId);

    /**
     * 根据用户所属部门被授予的角色的列表
     *
     * @param userId
     * @return
     */
    List<Long> getOrgAssignRoleList(Long userId);

    /**
     * 获取用户所有角色列表
     *
     * @param userId
     * @return
     */
    List<Long> getUserRoleList(Long userId);
}
