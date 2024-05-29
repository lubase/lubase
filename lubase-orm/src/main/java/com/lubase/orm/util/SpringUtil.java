package com.lubase.orm.util;

import org.springframework.context.ApplicationContext;

public class SpringUtil {
    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }
}
