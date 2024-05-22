package com.lcp.core.service;


import com.lcp.core.model.LoginUser;

/**
 * 获取当前登录用户信息。实现acs中app类的功能
 *
 * @author A
 */
public interface AppHolderService {
//    private ThreadLocal<LoginUser> user = new ThreadLocal<>();
//
//    public void setUser(LoginUser user) {
//        this.user.set(user);
//    }
//
//    public LoginUser getUser() {
//        return this.user.get();
//    }
//
//    public void clear() {
//        this.user.remove();
//    }

    void setUser(LoginUser user);
    LoginUser getUser();
    void clear();
}

