package com.lubase.core.service;

import com.lubase.orm.model.LoginUser;

import java.awt.image.BufferedImage;

/**
 * 登录验证码服务
 */
public interface VerifyCodeService {

    LoginUser checkVerifyCode(String vcode, String userCode);
    int calcErrorCount(String userCode);

    void clearCacheErrorCount(String userCode);

    BufferedImage getVerifyCode(String userCode);
}
