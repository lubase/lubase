package com.lcp.qibao.config;

import com.lcp.qibao.controller.handler.AuthenticationInterceptorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author A
 */
@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    @Autowired
    AuthenticationInterceptorInterceptor authenticationInterceptorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptorInterceptor).addPathPatterns("/**");
    }

    @Bean
    public AuthenticationInterceptorInterceptor authenticationInterceptorInterceptor() {
        return new AuthenticationInterceptorInterceptor();
    }
}
