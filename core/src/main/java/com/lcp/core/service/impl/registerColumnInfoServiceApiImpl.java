package com.lcp.core.service.impl;

import com.lcp.core.constant.CacheConst;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.core.service.RegisterColumnInfoService;
import com.lcp.coremodel.DbCode;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@CacheConfig(cacheNames = "tableStruct")
@Service("registerColumnInfoServiceApi")
@Slf4j
public class registerColumnInfoServiceApiImpl implements RegisterColumnInfoService {

    @Autowired
    RestTemplate restTemplate;
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    registerColumnInfoServiceApiImpl registerColumnInfoService;
    private String urlTemplate;

    private String fileUrlTemplate;
    @Value("${custom:cache-server}")
    private String cacheServer;

    public registerColumnInfoServiceApiImpl(Environment environment) {
        log.info("初始化url" + environment.toString());
        this.urlTemplate = String.format("%s/registerColumnInfo", environment.getProperty("custom.cache-server"));
        this.fileUrlTemplate = String.format("%s/fileInfo", environment.getProperty("custom.cache-server"));
        log.info("初始化url" + urlTemplate);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_TABLE + "+#tableId")
    @Override
    public DbTable initTableInfoByTableId(Long tableId) {
        String url = String.format("%s/initTableInfoByTableId?tableId=%s", urlTemplate, tableId);
        if (restTemplate.getForEntity(url, DbTable.class).getBody() == null) {
            return null;
        }
        return registerColumnInfoService.initTableInfoByTableId(tableId);
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

        if (restTemplate.getForEntity(url, DbTable.class).getBody() == null) {
            return null;
        }

        return registerColumnInfoService.initTableInfoByTableCode(tableCode);
    }

    @SneakyThrows
    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_NAME + "+#tableCode")
    @Override
    public String getTableIdByTableCode(String tableCode) {
        String url = String.format("%s/getTableIdByTableCode?tableCode=%s", urlTemplate, tableCode);
        if (restTemplate.getForEntity(url, Long.class).getBody() == null) {
            throw new WarnCommonException(String.format("表缓存获取失败：%s", tableCode));
        }
        return registerColumnInfoService.getTableIdByTableCode(tableCode);
    }

    @SneakyThrows
    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_NAME + "+#tableId")
    @Override
    public String getTableCodeByTableId(Long tableId) {
        String url = String.format("%s/getTableCodeByTableId?tableId=%s", urlTemplate, tableId);
        if (restTemplate.getForEntity(url, String.class).getBody() == null) {
            throw new WarnCommonException(String.format("表缓存获取失败：%s", tableId));
        }
        return registerColumnInfoService.getTableCodeByTableId(tableId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_COLUMN + "+#columnId")
    @Override
    public DbField getColumnInfoByColumnId(Long columnId) {
        String url = String.format("%s/getColumnInfoByColumnId?columnId=%s", urlTemplate, columnId);
        if (restTemplate.getForEntity(url, DbField.class).getBody() == null) {
            return null;
        }
        return registerColumnInfoService.getColumnInfoByColumnId(columnId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_COLUMNS + "+#tableId")
    @Override
    public List<DbField> getColumnsByTableId(Long tableId) {
        String url = String.format("%s/getColumnsByTableId?tableId=%s", urlTemplate, tableId);
        Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url, DbField[].class).getBody()));
        return registerColumnInfoService.getColumnsByTableId(tableId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_CONTROLLED_TABLE_LIST)
    @Override
    public List<String> getControlledTableList() {
        String url = String.format("%s/getControlledTableList", urlTemplate);
        if (restTemplate.getForEntity(url, long[].class).getBody() == null) {
            return new ArrayList<>();
        }
        return registerColumnInfoService.getControlledTableList();
    }

    @Cacheable(key = CacheConst.PRE_CACHE_CODE_DATA + "+#codeTypeId")
    @Override
    public List<DbCode> getCodeListByTypeId(String codeTypeId) {
        String url = String.format("%s/getCodeListByTypeId?codeTypeId=%s", urlTemplate, codeTypeId);
        if (restTemplate.getForEntity(url, DbCode[].class).getBody() == null) {
            return new ArrayList<>();
        }
        return registerColumnInfoService.getCodeListByTypeId(codeTypeId);
    }

    @Cacheable(value = "uploadFile", key = CacheConst.PRE_CACHE_FILE_DATA + "+#fileKey")
    @Override
    public List<DbEntity> getFileDisplayNameByFileKey(String fileKey) {
        String _dataId = fileKey.split("_")[0];
        String _fileKey = fileKey.split("_")[1];
        String url = String.format("%s/getFileDisplayNameByFileKey?dataId=%s&fileKey=%s", fileUrlTemplate, _dataId, _fileKey);
        log.info(url);
        Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url, DbEntity[].class).getBody()));
        return registerColumnInfoService.getFileDisplayNameByFileKey(fileKey);
    }

    @Cacheable(value = "uploadFile", key = CacheConst.PRE_CACHE_FILE_DATA + "+#fileKey")
    @Override
    public List<DbEntity> getFileDisplayNameByFileKey2(String fileKey) {
        String _dataId = fileKey.split("_")[0];
        String _fileKey = fileKey.substring(fileKey.indexOf("_") + 1);
        String url = String.format("%s/getFileDisplayNameByFileKey2?dataId=%s&fileKey=%s", fileUrlTemplate, _dataId, _fileKey);
        log.info(url);
        Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url, DbEntity[].class).getBody()));
        return registerColumnInfoService.getFileDisplayNameByFileKey2(fileKey);
    }
}
