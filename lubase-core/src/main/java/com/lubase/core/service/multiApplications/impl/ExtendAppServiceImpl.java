package com.lubase.core.service.multiApplications.impl;

import com.google.common.base.Preconditions;
import com.lubase.core.service.multiApplications.AppConfig;
import com.lubase.core.service.multiApplications.IAppInfo;
import com.lubase.core.service.multiApplications.IExtendAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtendAppServiceImpl  implements IExtendAppService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ExtendAppServiceImpl.class);
    @Autowired
    public ExtendAppManagerImpl ApplicationManager;
    @Autowired
    public LoadExtendApplicationImpl ApplicationLoader;

    @Override
    public IAppInfo loadAndRegister(AppConfig AppConfig) {
        Preconditions.checkNotNull(AppConfig, "AppConfig is null");
        //如果新应用可用,则加载并注册新应用,最后卸载旧应用
        if (AppConfig.getEnabled()) {
            IAppInfo app = ApplicationLoader.load(AppConfig);
            //    IAppInfo oldModule = ApplicationManager.register(app);
       //     destroyQuietly(oldModule);
            return app;
        }

        //新模块不可用则卸载旧模块
      //  removeApplication(AppConfig.getName());
        return null;
    }

    private IAppInfo removeApplication(String ApplicationName) {
        IAppInfo removed = ApplicationManager.remove(ApplicationName);
        destroyQuietly(removed);
        return removed;
    }

    private static void destroyQuietly(IAppInfo application) {
        if (application != null) {
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Destroy app: {}", application.getName());
                }
             //   application.destroy();
            } catch (Exception e) {
                LOGGER.error("Failed to destroy app " + application, e);
            }
        }
    }

    public void setModuleManager(ExtendAppManagerImpl ApplicationManager) {
        this.ApplicationManager = ApplicationManager;
    }

    public void setModuleLoader(LoadExtendApplicationImpl ApplicationLoader) {
        this.ApplicationLoader = ApplicationLoader;
    }
}
