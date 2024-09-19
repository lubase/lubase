package com.lubase.core.config;

import com.lubase.core.controller.handler.AuthenticationInterceptorInterceptor;
import com.lubase.core.service.UserInfoService;
import com.lubase.orm.service.AppHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author A
 */
@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {
    @Autowired
    AppHolderService appHolderService;
    @Autowired
    UserInfoService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AuthenticationInterceptorInterceptor interceptor = new AuthenticationInterceptorInterceptor(userService, appHolderService);
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }


}
