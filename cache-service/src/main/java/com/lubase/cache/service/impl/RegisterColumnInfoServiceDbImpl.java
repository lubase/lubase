package com.lubase.cache.service.impl;

import com.lubase.model.DbCode;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import com.lubase.model.ResourceDataModel;
import com.lubase.model.util.TypeConverterUtilsMirror;
import com.lubase.cache.constant.CacheConst;
import com.lubase.cache.mapper.CacheCoreTableMapper;
import com.lubase.cache.service.InitCacheService;
import com.lubase.cache.service.RegisterColumnInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * <p>
 * 表结构初始化类，仅用于controller中。其他场景请使用 registerColumnInfoServiceApi
 * </p>
 *
 * @author A
 * @since 2021-12-19
 */
@CacheConfig(cacheNames = "tableStruct")
@Slf4j
@Service
public class RegisterColumnInfoServiceDbImpl implements RegisterColumnInfoService, InitCacheService {
    @Autowired
    CacheCoreTableMapper coreTableMapper;

    @Autowired
    CacheManager cacheManager;

    @Override
    public void appStartInitCache() {
        // 初始化表名称和表id 的对应关系
        List<DbTable> list = coreTableMapper.getTableCodeAndIdInfo();
        HashMap<String, String> map = new HashMap<>();
        Cache cache = cacheManager.getCache(CacheConst.CACHE_NAME_TABLE_STRUCT);
        for (DbTable table : list) {
            cache.put(CacheConst.PRE_CACHE_TABLE_NAME.replace("'", "") + table.getId(), table.getCode());
            cache.put(CacheConst.PRE_CACHE_TABLE_NAME.replace("'", "") + table.getCode(), table.getId());
        }
        //初始化列信息和表信息
        for (DbTable table : list) {
            Long tableId = TypeConverterUtilsMirror.object2Long(table.getId());
            List<DbField> fieldList = getColumnsByTableId(tableId);
            cache.put(CacheConst.PRE_CACHE_COLUMNS.replace("'", "") + table.getId(), fieldList);
            for (DbField field : fieldList) {
                cache.put(CacheConst.PRE_CACHE_COLUMN.replace("'", "") + field.getId(), field);
            }
            table.setFieldList(fieldList);
            cache.put(CacheConst.PRE_CACHE_TABLE.replace("'", "") + tableId, table);
        }
        System.out.println("列信息和表信息初始化缓存成功");
    }

    @Cacheable(key = CacheConst.PRE_CACHE_TABLE + "+#tableId")
    @Override
    public DbTable initTableInfoByTableId(Long tableId) {
        if (tableId == null) {
            return null;
        }
        DbTable table = coreTableMapper.initTableInfoById(tableId);
        if (table == null) {
            return null;
        }
        List<DbField> fieldList = getColumnsByTableId(tableId);
        table.setFieldList(fieldList);
        return table;
    }

    /**
     * 临时方法，需要将parseLookupField 方法迁移走
     *
     * @param tableCode
     * @return
     */
    @Cacheable(key = CacheConst.PRE_CACHE_TABLE + "+#tableCode")
    @Override
    public DbTable initTableInfoByTableCode(String tableCode) {
        log.info("tableCode: " + tableCode);
        Long tableId = Long.parseLong(getTableIdByTableCode(tableCode));
        return initTableInfoByTableId(tableId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_NAME + "+#tableCode")
    @Override
    public String getTableIdByTableCode(String tableCode) {
        return coreTableMapper.getTableIdByCode(tableCode).toString();
    }

    @Cacheable(key = CacheConst.PRE_CACHE_TABLE_NAME + "+#tableId")
    @Override
    public String getTableCodeByTableId(Long tableId) {
        return coreTableMapper.getTableCodeById(tableId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_COLUMN + "+#columnId")
    @Override
    public DbField getColumnInfoByColumnId(Long columnId) {
        return coreTableMapper.initColumnInfoById(columnId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_COLUMNS + "+#tableId")
    @Override
    public List<DbField> getColumnsByTableId(Long tableId) {
        return coreTableMapper.initColumnInfoByTableId(tableId);
    }

    @Cacheable(key = CacheConst.PRE_CACHE_CONTROLLED_TABLE_LIST)
    @Override
    public List<String> getControlledTableList() {
        List<String> list = coreTableMapper.getControlledTableList();
        return list;
    }

    @Cacheable(key = CacheConst.PRE_CACHE_CODE_DATA + "+#codeTypeId")
    @Override
    public List<DbCode> getCodeListByTypeId(String codeTypeId) {
        return coreTableMapper.getCodeListByTypeId(codeTypeId);
    }

    @Override
    @Cacheable(value = CacheConst.CACHE_NAME_RESOURCE, key = "#appId" + "+':'" + "+#tableCode")
    public List<ResourceDataModel> getResourceList(String appId, String tableCode) {
        if (appId == null || StringUtils.isEmpty(appId) || tableCode == null || StringUtils.isEmpty(tableCode)) {
            return new ArrayList<>();
        }
        return coreTableMapper.getResourceList(appId, tableCode);
    }

}
