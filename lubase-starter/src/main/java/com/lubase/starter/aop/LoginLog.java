package com.lubase.starter.aop;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义登录日志
 * </p>
 * @author zhulz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {
    //用户code
    String userCode() default "";
}
