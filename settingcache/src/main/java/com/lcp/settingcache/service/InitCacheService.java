package com.lcp.settingcache.service;

/**
 * 缓存初始化接口，在使用缓存的服务中，请继承此接口
 */
public interface InitCacheService {
    /**
     * 初始化
     */
    void appStartInitCache();
}
