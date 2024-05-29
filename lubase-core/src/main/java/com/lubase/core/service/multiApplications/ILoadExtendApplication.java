package com.lubase.core.service.multiApplications;

import com.lubase.core.service.multiApplications.impl.AppInfoImpl;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;

import java.io.IOException;
import java.util.Set;

public interface ILoadExtendApplication {
    /**
     * 根据配置加载一个应用模块，创建一个新的ClassLoadr加载Application里的class，初始化Spring ApplicationContext等
     *
     * @param AppConfig 应用配置信息
     * @return 加载成功的应用
     */
    AppInfoImpl load(AppConfig AppConfig);

    AppInfoImpl autoLoad() throws IOException;

    /**
     * 从指定目录加载扩展文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    AppInfoImpl autoLoadFromDirectory(String path) throws IOException;

    AppConfig BuildAppConfigByEntity(ExtendFileModel fileModel) throws IOException;

    Set<Class<?>> getClassByType(Class<?> type);

    void RegisterInvoke(Set<Class<?>> invokeClasses);
}
