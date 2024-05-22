package com.lcp.qibao.service.multiApplications;

import java.util.Date;

public interface IAppInfo {

    /**
     * 应用名
     *
     * @return
     */
    String getName();

    /**
     * 应用版本号
     *
     * @return
     */
    String getVersion();

    /**
     * 应用的创建时间
     *
     * @return
     */
    Date getCreation();

    /**
     * 获取应用配置
     *
     * @return
     */
    AppConfig getAppConfig();
}
