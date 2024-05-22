package com.lcp.qibao.service.multiApplications;

/**
 * 扩展Application包服务类
 *
 *
 */
public interface IExtendAppService {

    /**
     * 加载并注册应用,会移除和卸载旧的应用
     *
     * @param AppConfig 应用配置信息
     * @return 注册成功的应用,如果应用不可用,则返回null
     */
    IAppInfo loadAndRegister(AppConfig AppConfig);

}
