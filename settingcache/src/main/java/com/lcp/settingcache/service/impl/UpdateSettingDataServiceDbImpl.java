package com.lcp.settingcache.service.impl;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.SsCacheEntity;
import com.lcp.settingcache.constant.CacheConst;
import com.lcp.settingcache.mapper.CacheCoreTableMapper;
import com.lcp.settingcache.service.InitCacheService;
import com.lcp.settingcache.service.UpdateSettingDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "tableTrigger")
@Slf4j
public class UpdateSettingDataServiceDbImpl implements UpdateSettingDataService, InitCacheService {


    @Autowired
    CacheCoreTableMapper coreTableMapper;

    @Autowired
    CacheManager cacheManager;


    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_CACHE_LIST)
    @Override
    public List<SsCacheEntity> getTableCacheSettingList() {
        List<SsCacheEntity> list = coreTableMapper.getTableCacheList();
        return list;
    }

    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_RELATE_LIST)
    @Override
    public List<DbEntity> getTableRelateSettingList() {
        List<DbEntity> list = coreTableMapper.getTableRelateList();
        return list;
    }

    @Override
    public void appStartInitCache() {
        List<SsCacheEntity> cacheSettingList = getTableCacheSettingList();
        Cache cache = cacheManager.getCache(CacheConst.CACHE_NAME_TABLE_TRIGGER);
        cache.put(CacheConst.PRE_CACHE_TABLE_CACHE_LIST.replace("'", ""), cacheSettingList);
        System.out.println("缓存表设置信息初始化缓存成功");
        List<DbEntity> relateSettingList = getTableRelateSettingList();
        cache.put(CacheConst.PRE_CACHE_TABLE_RELATE_LIST.replace("'", ""), relateSettingList);
        System.out.println("级联更新表信息初始化缓存成功");
    }
}
