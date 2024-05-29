package com.lubase.core.service.multiApplications.impl;

import com.google.common.collect.ImmutableList;
import com.lubase.core.service.multiApplications.IAppInfo;
import com.lubase.core.service.multiApplications.IExtendAppManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
@Service
public class ExtendAppManagerImpl  implements IExtendAppManager, DisposableBean{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ExtendAppManagerImpl.class);

    /**
     * 运行时模块,模块名:模块对象
     */
    private final ConcurrentHashMap<String, IAppInfo> Applications = new ConcurrentHashMap();

    /**
     * 加载模块错误信息
     */
    private final ConcurrentHashMap<String, String> errorContext = new ConcurrentHashMap();

    @Override
    public List<IAppInfo> getApps() {
        return ImmutableList
                .copyOf(filter(Applications.values(), instanceOf(AppInfoImpl.class)));
    }

    @Override
    public IAppInfo find(String name) {
        checkNotNull(name, "app name is null");
        return Applications.get(name.toUpperCase());
    }

    @Override
    public IAppInfo register(IAppInfo Application) {
        checkNotNull(Application, "app is null");
        String name = Application.getName();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Put app: {}-{}", name, Application.getVersion());
        }

        return Applications.put(name.toUpperCase(Locale.CHINESE), Application);
    }

    @Override
    public IAppInfo remove(String name) {
        checkNotNull(name, "app name is null");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Remove app: {}", name);
        }
        return Applications.remove(name.toUpperCase());
    }

    @Override
    public Map<String, String> getErrorAppContext() {
        return errorContext;
    }

    @Override
    public void destroy() throws Exception {

    }
}
