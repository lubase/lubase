package com.lubase.orm.service.update.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.model.CachePathModel;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.TableUpdateSettingCacheDataService;
import com.lubase.orm.service.query.DataAccessQueryCoreService;
import com.lubase.orm.service.update.UpdateTriggerService;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.model.SsCacheEntity;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据更新后自动更新缓存逻辑
 */
@Component
@Order(3)
public class UpdateCacheTriggerServiceImpl implements UpdateTriggerService {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    TableUpdateSettingCacheDataService cacheDataService;
    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;

    private List<SsCacheEntity> getTableCacheSettingList(String tableCode) {
        List<SsCacheEntity> list = cacheDataService.getTableCacheSettingList()
                .stream().filter(c -> c.getTable_code().equals(tableCode)).collect(Collectors.toList());
        //  临时这么处理
        if (CacheConst.ENABLE_CACHE_TABLE.contains(tableCode)) {
            SsCacheEntity cacheEntity = new SsCacheEntity();
            cacheEntity.setCache_name(CacheConst.CACHE_TABLE_DATA);
            cacheEntity.setTable_code(tableCode);
            cacheEntity.setUpdate_event(7);
            cacheEntity.setKey_field("id");
            cacheEntity.setCache_key_pre(String.format("%s:", tableCode));
            list.add(cacheEntity);
        }
        return list;
    }

    @SneakyThrows
    @Override
    public void beforeUpdate(DbCollection collection) {
        //获取需要清除的缓存列表
        List<CachePathModel> cacheKeyList = new ArrayList<>();
        String tableCode = collection.getTableName();
        List<SsCacheEntity> cacheSettingModelList = getTableCacheSettingList(tableCode);
        if (cacheSettingModelList.size() == 0) {
            //如果此表无缓存配置则跳过
            return;
        }
        // 删除时需要在事前触发器进行缓存清除
        // 新增和修改时需要在时候触发器进行清除
        for (DbEntity entity : collection.getData()) {
            if (entity.getDataState().equals(EDBEntityState.Deleted)) {
                getCacheKeyFromDataEntity(cacheSettingModelList, tableCode, entity, cacheKeyList);
            }
        }
        if (cacheKeyList.size() > 0) {
            collection.setObjState(cacheKeyList);
            // 避免缓存顺序问题，所以清除两遍
            clareCache(cacheKeyList);
        }
    }

    @SneakyThrows
    @Override
    public void afterUpdate(DbCollection collection, Integer updateRowCount) {
        String tableCode = collection.getTableName();
        List<SsCacheEntity> cacheSettingModelList = getTableCacheSettingList(tableCode);
        if (cacheSettingModelList.size() == 0) {
            //如果此表无缓存配置则跳过
            return;
        }
        List<CachePathModel> cacheKeyList = new ArrayList<>();
        Object objState = collection.getObjState();
        if (objState instanceof ArrayList) {
            cacheKeyList = (ArrayList<CachePathModel>) objState;
        }
        for (DbEntity entity : collection.getData()) {
            if (entity.getDataState().equals(EDBEntityState.Modified) || entity.getDataState().equals(EDBEntityState.Added)) {
                getCacheKeyFromDataEntity(cacheSettingModelList, tableCode, entity, cacheKeyList);
            }
        }

        clareCache(cacheKeyList);
    }

    void getCacheKeyFromDataEntity(List<SsCacheEntity> cacheSettingModelList, String tableCode, DbEntity entity, List<CachePathModel> cacheKeyList) throws InvokeCommonException {
        for (SsCacheEntity cacheSettingModel : cacheSettingModelList) {
            if ((cacheSettingModel.getUpdate_event() | entity.getDataState().getIndex()) != cacheSettingModel.getUpdate_event()) {
                continue;
            }
            String cacheKey = "";
            if (!entity.containsKey(cacheSettingModel.getKey_field()) || null == entity.get(cacheSettingModel.getKey_field())) {
                String tmpKeyField = getCacheKeyFromDb(tableCode, cacheSettingModel.getKey_field(), entity.getId());
                if (!StringUtils.isEmpty(tmpKeyField)) {
                    cacheKey = String.format("%s%s", cacheSettingModel.getCache_key_pre(), tmpKeyField);
                } else {
                    //continue;
                    throw new InvokeCommonException(String.format("缓存配置错误，表代码：%s，keyField %s 不存在", tableCode, cacheSettingModel.getKey_field()));
                }
            } else {
                cacheKey = String.format("%s%s", cacheSettingModel.getCache_key_pre(), entity.get(cacheSettingModel.getKey_field()).toString());
            }
            CachePathModel model = new CachePathModel(cacheSettingModel.getCache_name(), cacheKey);
            if (cacheKeyList.stream().filter(c -> c.getFullPath().equals(model.getFullPath())).count() == 0) {
                cacheKeyList.add(model);
            }
        }
    }

    void clareCache(List<CachePathModel> cacheKeyList) {
        // 避免缓存顺序问题，所以清除两遍
        for (int i = 0; i < 2; i++) {
            for (CachePathModel model : cacheKeyList) {
                Cache cache = cacheManager.getCache(model.getCacheName());
                if (cache != null) {
                    cache.evict(model.getCacheKey());
                }
            }
        }
    }

    String getCacheKeyFromDb(String tableCode, String keyField, Long dataId) {
        QueryOption queryOption = new QueryOption(tableCode);
        queryOption.setFixField(keyField);
        queryOption.setTableFilter(new TableFilter("id", dataId, EOperateMode.Equals));
        queryOption.setBuildLookupField(false);
        DbCollection collection = dataAccessQueryCoreService.query(queryOption, false);
        if (collection.getData().size() == 1) {
            return TypeConverterUtils.object2String(collection.getData().get(0).get(keyField));
        } else {
            return "";
        }
    }
}
