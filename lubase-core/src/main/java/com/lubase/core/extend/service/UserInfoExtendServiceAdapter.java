package com.lubase.core.extend.service;

import com.lubase.core.extend.UserLoginExtendService;
import com.lubase.core.extend.UserSelectForComponentDataService;

/**
 * 用户信息服务扩展
 */
public interface UserInfoExtendServiceAdapter {
    /**
     * 获取选人服务
     *
     * @return
     */
    UserSelectForComponentDataService getComponentDataForSelectUserService();

    /**
     * 获取登录服务
     * @return
     */
    UserLoginExtendService getUserLoginExtendService();
}
