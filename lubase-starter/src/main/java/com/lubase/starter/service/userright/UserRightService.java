package com.lubase.starter.service.userright;

import com.lubase.starter.service.userright.model.UserRightInfo;

import java.util.List;

public interface UserRightService {
    /**
     * 获取用于权限信息。此方法应该加入缓存
     *
     * @param userId
     * @return
     */
    UserRightInfo getUserRight(Long userId);

    /**
     * 获取用户有权限的应用列表
     *
     * @param userId
     * @return
     */
    List<Long> getUserAllApp(Long userId);

    /**
     * 检查是否有功能权限
     *
     * @param rightInfo
     * @param funcId
     * @return
     */
    Boolean checkFuncRight(UserRightInfo rightInfo, Long funcId);
}
