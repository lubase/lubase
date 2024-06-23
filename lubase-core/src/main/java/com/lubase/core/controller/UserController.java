package com.lubase.core.controller;

import com.lubase.core.model.LoginInfoModel;
import com.lubase.core.service.VerifyCodeService;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.util.ServerMacroService;
import com.lubase.core.aop.LoginLog;
import com.lubase.core.aop.LoginInfoVO;
import com.lubase.core.config.PassToken;
import com.lubase.core.exception.LoginErrorException;
import com.lubase.core.response.ResponseData;
import com.lubase.core.service.UserInfoService;
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

    @Autowired
    VerifyCodeService verifyCodeService;

    @LoginLog
    @PassToken
    @PostMapping(value = "login")
    public ResponseData<LoginUser> login(@RequestBody LoginInfoVO loginInfo) {
        if (loginInfo == null || StringUtils.isEmpty(loginInfo.getUid()) || StringUtils.isEmpty(loginInfo.getPwd())) {
            return ResponseData.parameterNotFound("uid or pwd");
        }
        try {
            LoginInfoModel infoModel = userService.userLogin(loginInfo.getUid(), loginInfo.getPwd(), loginInfo.getVcode());
            if (infoModel.getLoginErrorException() != null) {
                return ResponseData.error(infoModel.getLoginErrorException().getErrorCode(), infoModel.getLoginErrorException().getErrorMsg());
            } else {
                LoginUser user = infoModel.getLoginUser();
                return ResponseData.success(user);
            }
        } catch (LoginErrorException exception) {
            return ResponseData.error(exception.getErrorCode(), exception.getErrorMsg());
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
        var code = verifyCodeService.getVerifyCode(String.valueOf(request.getParameter("uid")));
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
