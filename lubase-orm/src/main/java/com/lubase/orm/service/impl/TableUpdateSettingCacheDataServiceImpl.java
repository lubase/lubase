package com.lubase.orm.service.impl;

import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.service.TableUpdateSettingCacheDataService;
import com.lubase.model.DbEntity;
import com.lubase.model.SsCacheEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TableUpdateSettingCacheDataServiceImpl implements TableUpdateSettingCacheDataService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TableUpdateSettingCacheDataService tableUpdateSettingCacheDataService;

    private String urlTemplate;

    public TableUpdateSettingCacheDataServiceImpl(Environment environment) {
        this.urlTemplate = String.format("%s/registerColumnInfo", environment.getProperty("custom.cache-server"));
    }

    @Cacheable(value = CacheConst.CACHE_NAME_TABLE_TRIGGER, key = CacheConst.PRE_CACHE_TABLE_CACHE_LIST)
    @Override
    public List<SsCacheEntity> getTableCacheSettingList() {
        String url = String.format("%s/getTableCacheSettingList", urlTemplate);
        restTemplate.getForEntity(url, DbEntity[].class).getBody();
        return tableUpdateSettingCacheDataService.getTableCacheSettingList();
    }

    @Cacheable(value = CacheConst.CACHE_NAME_TABLE_TRIGGER, key = CacheConst.PRE_CACHE_TABLE_RELATE_LIST)
    @Override
    public List<DbEntity> getTableRelateSettingList() {
        String url = String.format("%s/getTableRelateSettingList", urlTemplate);
        restTemplate.getForEntity(url, DbEntity[].class).getBody();
        return tableUpdateSettingCacheDataService.getTableRelateSettingList();
    }

}