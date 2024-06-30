package com.lubase.sso.service;


import com.lubase.core.aop.LoginInfoVO;
import com.lubase.orm.model.LoginUser;

public interface ISSOUserService {
    LoginUser getSSOUser(LoginInfoVO loginInfo);
}
