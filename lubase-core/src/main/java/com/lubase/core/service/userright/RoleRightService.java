package com.lubase.core.service.userright;

import com.lubase.core.service.userright.model.ColumnRightModelVO;
import com.lubase.core.service.userright.model.RoleModel;

import java.util.List;

/**
 * 此类应该仅服务于权限设置，其它功能应该迁移走
 */
public interface RoleRightService {
    /**
     * 获取角色所属的appId
     *
     * @param roleId
     * @return
     */
    RoleModel getRoleInfoById(Long roleId);

    /**
     * 获取角色功能权限信息
     *
     * @param roleId
     * @return
     */
    List<Long> getRoleFuncList(Long roleId);

    /**
     * 获取具体角色的字段权限
     *
     * @param roleId
     * @return
     */
    List<ColumnRightModelVO> getRoleColList(Long roleId);
}
