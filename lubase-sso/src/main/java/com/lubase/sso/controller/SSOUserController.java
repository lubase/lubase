package com.lubase.sso.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lubase.orm.service.AppHolderService;
import com.lubase.core.config.PassToken;
import com.lubase.orm.model.LoginUser;
import com.lubase.sso.model.SSODataBo;
import com.lubase.sso.model.SSOResultDataBo;
import com.lubase.core.aop.LoginInfoVO;
import com.lubase.core.response.ResponseData;
import com.lubase.core.service.UserInfoService;
import com.lubase.sso.service.Impl.SSOUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * sso用户登录、密码服务等
 */
@PassToken
@RestController
@RequestMapping("/ssouser")
public class SSOUserController {

    @Autowired
    SSOUserServiceImpl ssoUserService;

    @Autowired
    UserInfoService userInfoService;

    @PassToken
    @PostMapping(value = "ssologin")
    public ResponseData<LoginUser> login(@RequestBody LoginInfoVO loginInfo) {
        if (loginInfo == null || StringUtils.isEmpty(loginInfo.getPwd())) {
            return ResponseData.parameterNotFound("token");
        }
        //pwd 存放sso_token
        var user = ssoUserService.getSSOUser(loginInfo);
        if (user == null) {
            return ResponseData.error("401", "用户名或密码错误");
        } else {
            user.setToken(userInfoService.createUserToken(user));
            return ResponseData.success(user);
        }
    }

    /**
     * 测试接口
     * @param access_token
     * @param platform_code
     * @return
     */
    @PassToken
    @GetMapping(value = "authenticate/check_token")
    public SSODataBo getSSOData(@RequestParam String access_token, @RequestParam String platform_code) {
        SSODataBo ssoDataBo = new SSODataBo();
        SSOResultDataBo ssoResultDataBo = new SSOResultDataBo();
        DecodedJWT decodedJWT = JWT.decode(access_token);
        var userId = Long.valueOf(decodedJWT.getAudience().get(0));
        var code = decodedJWT.getClaim("uco").asString();
        var name = decodedJWT.getClaim("una").asString();
       var  orgId = decodedJWT.getClaim("uor").asString();
        ssoResultDataBo.setUser_code(code);
        ssoResultDataBo.setUser_name(name);
        ssoResultDataBo.setGroup_id("1111111");
        ssoResultDataBo.setGroup_name("测试-部门");
        ssoDataBo.setResult(ssoResultDataBo);
        ssoDataBo.setKey("S_0000");
        return ssoDataBo;
    }
}
