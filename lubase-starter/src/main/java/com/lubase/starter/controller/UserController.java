package com.lubase.starter.controller;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.exception.ParameterNotFoundException;
import com.lubase.core.model.LoginUser;
import com.lubase.core.service.AppHolderService;
import com.lubase.core.util.ServerMacroService;
import com.lubase.starter.aop.LoginLog;
import com.lubase.starter.aop.LoginInfoVO;
import com.lubase.starter.config.PassToken;
import com.lubase.starter.exception.LoginErrorException;
import com.lubase.starter.response.ResponseData;
import com.lubase.starter.service.UserInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户登录、密码服务等
 *
 * @author A
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserInfoService userService;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    ServerMacroService serverMacroService;

    @LoginLog
    @PassToken
    @PostMapping(value = "login")
    public ResponseData<LoginUser> login(@RequestBody LoginInfoVO loginInfo) {
        if (loginInfo == null || StringUtils.isEmpty(loginInfo.getUid()) || StringUtils.isEmpty(loginInfo.getPwd())) {
            return ResponseData.parameterNotFound("uid or pwd");
        }
        LoginUser user;
        try {
            user = userService.getUser(loginInfo.getUid(), loginInfo.getPwd(), loginInfo.getVcode());
        } catch (LoginErrorException exception) {
            return ResponseData.error(exception.getErrorCode(), exception.getErrorMsg());
        }
        if (user.getErrorCount() > 0) {
            //临时这样写，通用逻辑再优化
            if (user.getErrorCount() < 5) {
                return ResponseData.error("401", "用户名或密码错误");
            } else {
                //约定420  需要验证码
                return ResponseData.error("420", "用户名或密码错误");
            }
        } else if (user.getErrorCount() < 0) {
            if (user.getErrorCount() == -1) {
                return ResponseData.error("421", "输入验证码错误");
            } else if (user.getErrorCount() == -2) {
                return ResponseData.error("422", "验证码已失效");
            } else {
                return ResponseData.error("423", "验证码为空");
            }
        } else {
            user.setToken(userService.createUserToken(user));
            return ResponseData.success(user);
        }
    }

    @PostMapping(value = "info")
    public ResponseData<LoginUser> getUserInfo() {
        LoginUser user = appHolderService.getUser();
        return ResponseData.success(user);
    }

    @SneakyThrows
    @RequestMapping(value = "modifyUserPwd", method = RequestMethod.POST)
    public ResponseData<Integer> modifyUserPwd(@RequestBody Map<String, String> map) {
        LoginUser user = appHolderService.getUser();
        //String userCode = map.get("userCode");
        String newPwd = map.get("newPwd");
        String oldPwd = map.get("oldPwd");
        if (StringUtils.isEmpty(oldPwd)) {
            return ResponseData.error("原密码为空");
        }
        var tip = userService.validatePassword(newPwd);
        if (!StringUtils.isEmpty(tip)) {
            return ResponseData.error(tip);
        }
        Integer number = userService.modifyUserPwd(user.getCode(), newPwd, oldPwd);
        if (number == -10) {
            return ResponseData.error("原密码输入错误");
        }
        return ResponseData.success(number);
    }

    @SneakyThrows
    @RequestMapping(value = "modifyUserPwdT", method = RequestMethod.POST)
    public ResponseData<Integer> modifyUserPwdT(@RequestBody Map<String, String> map) {
        LoginUser user = appHolderService.getUser();
        String newPwd = map.get("newPwd");
        Integer number = userService.modifyUserPwd(user.getCode(), newPwd, null);
        return ResponseData.success(number);
    }

    @PassToken
    @SneakyThrows
    @RequestMapping(value = "verifyCode", method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(request.getParameter("uid"))) {
            throw new InvokeCommonException("用户信息为空");
        }
        var code = userService.getVerifyCode(String.valueOf(request.getParameter("uid")));
        ImageIO.write(code, "jpg", response.getOutputStream());
    }

    @SneakyThrows
    @RequestMapping(value = "getServerMacro", method = RequestMethod.POST)
    public ResponseData<String> getServerMacro(String serverMacro) {
        if (StringUtils.isEmpty(serverMacro)) {
            throw new ParameterNotFoundException("serverMacro");
        }
        return ResponseData.success(serverMacroService.getServerMacroByKey(serverMacro));
    }
}
