package com.lubase.starter.controller.handler;

import com.lubase.core.model.LoginUser;
import com.lubase.core.service.AppHolderService;
import com.lubase.starter.config.PassToken;
import com.lubase.starter.service.UserInfoService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器，对认证信息进行校验
 *
 * @author A
 */
public class AuthenticationInterceptorInterceptor implements HandlerInterceptor {

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    UserInfoService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;

        if (method.getMethod().isAnnotationPresent(PassToken.class) || method.getClass().isAnnotationPresent(PassToken.class)) {
            return true;
        }
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
            if (StringUtils.isEmpty(token)) {
                System.out.println("not found  token");
                throw new AuthenticationException("not found  token");
            }
        }
        LoginUser user = userService.verifyToken(token);
        if (user == null) {
            throw new AuthenticationException("用户信息已经失效");
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        appHolderService.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
