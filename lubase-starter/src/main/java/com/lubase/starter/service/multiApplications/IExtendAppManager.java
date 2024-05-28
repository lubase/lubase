package com.lubase.starter.service.multiApplications;

import java.util.List;
import java.util.Map;

public interface IExtendAppManager {
    /**
     * 根据模块名查找Application包
     * @param name
     * @return IApplicationInfo
     */
    IAppInfo find(String name);

    /**
     * 获取所有已加载的Application包
     *
     * @return  List<IApplicationInfo>
     */
    List<IAppInfo> getApps();

    /**
     * 注册一个多应用
     *
     * @param ApplicationInfo Application包信息
     * @return 旧模块,如果没有旧模块则返回null
     */
    IAppInfo register(IAppInfo ApplicationInfo);

    /**
     * 移除一个多应用
     *
     * @param name 应用名
     * @return 被移除的模块
     */
    IAppInfo remove(String name);

    /**
     * 获取发布失败的模块异常信息
     *
     * @return key:应用名,value:错误信息
     */
    Map<String, String> getErrorAppContext();
}
