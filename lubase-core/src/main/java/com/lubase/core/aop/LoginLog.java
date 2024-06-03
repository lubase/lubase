package com.lubase.core.aop;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义登录日志
 * </p>
 * @author bluesky
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {
    //用户code
    String userCode() default "";
}
