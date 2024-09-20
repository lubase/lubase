package com.lubase.orm.service.impl;

import com.lubase.model.DbCode;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.service.RegisterColumnInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames = "tableStruct")
@Service("registerColumnInfoServiceApi")
@Slf4j
public class RegisterColumnInfoServiceApiImpl implements RegisterColumnInfoService {

    @Autowired
    RestTemplate restTemplate;

    private String urlTemplate;

    private String fileUrlTemplate;

    @Autowired
    CacheManager cacheManager;

    public RegisterColumnInfoServiceApiImpl(Environment environment) {
        log.info("初始化url" + environment.toString());
        this.urlTemplate = String.format("%s/registerColumnInfo", environment.getProperty("lubase.cache-server"));
        this.fileUrlTemplate = String.format("%s/fileInfo", environment.getProperty("lubase.cache-server"));
        log.info("初始化url" + urlTemplate);
    }

    private String getCacheKey(String pre, String dataId) {
        return (pre + dataId).replace("'", "");
    }

    private Cache getCache() {
        return cacheManager.getCache(CacheConst.CACHE_NAME_TABLE_STRUCT);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_TABLE + "+#tableId")
    @Override
    public DbTable initTableInfoByTableId(Long tableId) {
        String url = String.format("%s/initTableInfoByTableId?tableId=%s", urlTemplate, tableId);
        if (restTemplate.getForEntity(url, DbTable.class).getBody() == null) {
            return null;
        }
        Cache cache = getCache();
        if (cache != null) {
            return cache.get(getCacheKey(CacheConst.PRE_CACHE_TABLE, tableId.toString()), DbTable.class);
        } else {
            return null;
        }
    }

    @SneakyThrows
    @Cacheable(key = CacheConst.PRE_CACHE_TABLE + "+#tableCode")
    @Override
    public DbTable initTableInfoByTableCode(String tableCode) {
        if (StringUtils.isEmpty(tableCode)) {
            return null;
        }
        String url = String.format("%s/initTableInfoByTableCode?tableCode=%s", urlTemplate, tableCode);
        log.info("url" + url);

        try {
            if (restTemplate.getForEntity(url, DbTable.class).getBody() == null) {
                log.error("未找到表结构：" + tableCode);
                return null;
            }
        } catch (Exception e) {
            log.error("未找到表结构：" + tableCode);
            return null;
        }
        Cache cache = getCache();
        if (cache != null) {
            return cache.get(getCacheKey(CacheConst.PRE_CACHE_TABLE, tableCode), DbTable.class);
        } else {
            return null;
        }
    }

    @SneakyThrows
    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_NAME + "+#tableCode")
    @Override
    public String getTableIdByTableCode(String tableCode) {
        String url = String.format("%s/getTableIdByTableCode?tableCode=%s", urlTemplate, tableCode);
        if (restTemplate.getForEntity(url, Long.class).getBody() == null) {
            throw new WarnCommonException(String.format("表缓存获取失败：%s", tableCode));
        }
        Cache cache = getCache();
        if (cache != null) {
            return cache.get(getCacheKey(CacheConst.PRE_CACHE_TABLE_NAME, tableCode), String.class);
        } else {
            return null;
        }
    }

    @SneakyThrows
    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_NAME + "+#tableId")
    @Override
    public String getTableCodeByTableId(Long tableId) {
        String url = String.format("%s/getTableCodeByTableId?tableId=%s", urlTemplate, tableId);
        if (restTemplate.getForEntity(url, String.class).getBody() == null) {
            throw new WarnCommonException(String.format("表缓存获取失败：%s", tableId));
        }
        Cache cache = getCache();
        if (cache != null) {
            return cache.get(getCacheKey(CacheConst.PRE_CACHE_TABLE_NAME, tableId.toString()), String.class);
        } else {
            return null;
        }
    }

    @Cacheable(key = CacheConst.PRE_CACHE_COLUMN + "+#columnId")
    @Override
    public DbField getColumnInfoByColumnId(Long columnId) {
        String url = String.format("%s/getColumnInfoByColumnId?columnId=%s", urlTemplate, columnId);
        if (restTemplate.getForEntity(url, DbField.class).getBody() == null) {
            return null;
        }
        Cache cache = getCache();
        if (cache != null) {
            return cache.get(getCacheKey(CacheConst.PRE_CACHE_COLUMN, columnId.toString()), DbField.class);
        } else {
            return null;
        }
    }

    @Cacheable(key = CacheConst.PRE_CACHE_COLUMNS + "+#tableId")
    @Override
    public List<DbField> getColumnsByTableId(Long tableId) {
        String url = String.format("%s/getColumnsByTableId?tableId=%s", urlTemplate, tableId);
        restTemplate.getForEntity(url, DbField[].class).getBody();

        Cache cache = getCache();
        if (cache != null) {
            Cache.ValueWrapper obj = cache.get(getCacheKey(CacheConst.PRE_CACHE_COLUMNS, tableId.toString()));
            if (obj != null && obj.get() instanceof List<?>) {
                return (List<DbField>) obj.get();
            }
        }
        return null;
    }

    @Cacheable(key = CacheConst.PRE_CACHE_CONTROLLED_TABLE_LIST)
    @Override
    public List<String> getControlledTableList() {
        String url = String.format("%s/getControlledTableList", urlTemplate);
        if (restTemplate.getForEntity(url, long[].class).getBody() == null) {
            return new ArrayList<>();
        }
        Cache cache = getCache();
        if (cache != null) {
            Cache.ValueWrapper obj = cache.get(getCacheKey(CacheConst.PRE_CACHE_CONTROLLED_TABLE_LIST, ""));
            if (obj != null && obj.get() instanceof List<?>) {
                return (List<String>) obj.get();
            }
        }
        return null;
    }

    @Cacheable(key = CacheConst.PRE_CACHE_CODE_DATA + "+#codeTypeId")
    @Override
    public List<DbCode> getCodeListByTypeId(String codeTypeId) {
        String url = String.format("%s/getCodeListByTypeId?codeTypeId=%s", urlTemplate, codeTypeId);
        if (restTemplate.getForEntity(url, DbCode[].class).getBody() == null) {
            return new ArrayList<>();
        }
        Cache cache = getCache();
        if (cache != null) {
            Cache.ValueWrapper obj = cache.get(getCacheKey(CacheConst.PRE_CACHE_CODE_DATA, codeTypeId));
            if (obj != null && obj.get() instanceof List<?>) {
                return (List<DbCode>) obj.get();
            }
        }
        return null;
    }


    @Cacheable(value = "uploadFile", key = CacheConst.PRE_CACHE_FILE_DATA + "+#fileKey")
    @Override
    public List<DbEntity> getFileDisplayNameByFileKey(String fileKey) {
        String _dataId = fileKey.split("_")[0];
        String _fileKey = fileKey.substring(fileKey.indexOf("_") + 1);
        String url = String.format("%s/getFileDisplayNameByFileKey2?dataId=%s&fileKey=%s", fileUrlTemplate, _dataId, _fileKey);

        restTemplate.getForEntity(url, DbEntity[].class).getBody();
        Cache cache = cacheManager.getCache("uploadFile");
        if (cache != null) {
            Cache.ValueWrapper obj = cache.get(getCacheKey(CacheConst.PRE_CACHE_FILE_DATA, fileKey));
            if (obj != null && obj.get() instanceof List<?>) {
                return (List<DbEntity>) obj.get();
            }
        }
        return null;
    }
}
