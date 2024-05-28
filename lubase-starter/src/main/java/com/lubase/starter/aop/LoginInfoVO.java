package com.lubase.starter.aop;

import lombok.Data;

/**
 * 登录信息
 *
 * @author A
 */
@Data
public class LoginInfoVO {
    private String uid;
    private String userCode;
    private String pwd;
    private String vcode;
}
