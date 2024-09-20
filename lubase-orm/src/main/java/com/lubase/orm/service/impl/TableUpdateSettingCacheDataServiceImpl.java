package com.lubase.orm.service.impl;

import com.lubase.model.DbEntity;
import com.lubase.model.SsCacheEntity;
import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.service.TableUpdateSettingCacheDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TableUpdateSettingCacheDataServiceImpl implements TableUpdateSettingCacheDataService {

    @Autowired
    RestTemplate restTemplate;

    private String urlTemplate;

    @Autowired
    CacheManager cacheManager;

    public TableUpdateSettingCacheDataServiceImpl(Environment environment) {
        this.urlTemplate = String.format("%s/registerColumnInfo", environment.getProperty("lubase.cache-server"));
    }

    @Cacheable(value = CacheConst.CACHE_NAME_TABLE_TRIGGER, key = CacheConst.PRE_CACHE_TABLE_CACHE_LIST)
    @Override
    public List<SsCacheEntity> getTableCacheSettingList() {
        String url = String.format("%s/getTableCacheSettingList", urlTemplate);
        restTemplate.getForEntity(url, DbEntity[].class).getBody();

        Cache cache = cacheManager.getCache(CacheConst.CACHE_NAME_TABLE_TRIGGER);
        String key = CacheConst.PRE_CACHE_TABLE_CACHE_LIST.replace("'", "");
        if (cache != null) {
            Cache.ValueWrapper obj = cache.get(key);
            if (obj != null && obj.get() instanceof List<?>) {
                return (List<SsCacheEntity>) obj.get();
            }
        }
        return null;
    }

    @Cacheable(value = CacheConst.CACHE_NAME_TABLE_TRIGGER, key = CacheConst.PRE_CACHE_TABLE_RELATE_LIST)
    @Override
    public List<DbEntity> getTableRelateSettingList() {
        String url = String.format("%s/getTableRelateSettingList", urlTemplate);
        restTemplate.getForEntity(url, DbEntity[].class).getBody();

        Cache cache = cacheManager.getCache(CacheConst.CACHE_NAME_TABLE_TRIGGER);
        String key = CacheConst.PRE_CACHE_TABLE_RELATE_LIST.replace("'", "");
        if (cache != null) {
            Cache.ValueWrapper obj = cache.get(key);
            if (obj != null && obj.get() instanceof List<?>) {
                return (List<DbEntity>) obj.get();
            }
        }
        return null;
    }

}
