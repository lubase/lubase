package com.lubase.starter.service.multiApplications.impl;

import com.lubase.starter.service.multiApplications.AppConfig;
import com.lubase.starter.service.multiApplications.IAppInfo;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.context.ConfigurableApplicationContext;

import java.beans.Introspector;
import java.util.Date;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;


public class AppInfoImpl  implements IAppInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppInfoImpl.class);

    /**  模块的配置信息 */
    com.lubase.starter.service.multiApplications.AppConfig AppConfig;

    /**  模块的名称 */
    private final String name;

    /**  模块的版本 */
    private final String version;

    /**  模块启动的时间 */
    private final Date creation;

    public AppInfoImpl(AppConfig AppConfig,String version, String name) {
        this.AppConfig = AppConfig;
        this.version = version;

        this.name = name;
        this.creation = new Date();
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Date getCreation() {
        return creation;
    }


    /**
     * 清除类加载器
     *
     * @param classLoader
     */
    public static void clear(ClassLoader classLoader) {
        checkNotNull(classLoader, "classLoader is null");
        //Introspector缓存BeanInfo类来获得更好的性能。卸载时刷新所有Introspector的内部缓存。
        Introspector.flushCaches();
        //从已经使用给定类加载器加载的缓存中移除所有资源包
        ResourceBundle.clearCache(classLoader);
        //Clear the introspection cache for the given ClassLoader
        CachedIntrospectionResults.clearClassLoader(classLoader);
        LogFactory.release(classLoader);
    }

    /**
     * 关闭Spring上下文
     * @param applicationContext
     */
    private static void closeQuietly(ConfigurableApplicationContext applicationContext) {
        checkNotNull(applicationContext, "applicationContext is null");
        try {
            applicationContext.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close application context", e);
        }
    }

    @Override
    public AppConfig getAppConfig() {
        return AppConfig;
    }


}
