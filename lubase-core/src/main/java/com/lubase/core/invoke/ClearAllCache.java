package com.lubase.core.invoke;

import com.lubase.core.extend.IInvokeMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.HashMap;
import java.util.Objects;

public class ClearAllCache implements IInvokeMethod {

    @Autowired
    CacheManager cacheManager;

    @Override
    public String getDescription() {
        return "清除所有的缓存数据 ";
    }

    @Override
    public Object exe(HashMap<String, String> mapParam) throws Exception {
        cacheManager.getCacheNames().forEach(cache -> Objects.requireNonNull(cacheManager.getCache(cache)).invalidate());
        return true;
    }

    @Override
    public String getId() {
        return "703399764061851648";
    }
}
