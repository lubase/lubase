package com.lubase.core.extend;


import com.lubase.core.model.LoginInfoModel;


/**
 * 用户信息扩展
 */
public interface UserLoginExtendService {

    /**
     * 根据用户名密码进行登录
     *
     * @param userId
     * @param password
     */
    LoginInfoModel userLogin(String userId, String password);
}
