package com.lubase.core.service.update.impl;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.model.CachePathModel;
import com.lubase.core.model.DbCollection;
import com.lubase.core.operate.EOperateMode;
import com.lubase.core.service.TableUpdateSettingCacheDataService;
import com.lubase.core.service.query.DataAccessQueryCoreService;
import com.lubase.core.service.update.UpdateTriggerService;
import com.lubase.core.util.TypeConverterUtils;
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
        return cacheDataService.getTableCacheSettingList()
                .stream().filter(c -> c.getTable_code().equals(tableCode)).collect(Collectors.toList());
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
            if(cacheSettingModel.getKey_field().equals("*")){
                //说明任何一个字段变化都清除缓存
                cacheKey=cacheSettingModel.getCache_key_pre();
            }
            else {
                if (!entity.containsKey(cacheSettingModel.getKey_field()) || null == entity.get(cacheSettingModel.getKey_field())) {
                    String tmpKeyField = getCacheKey(tableCode, cacheSettingModel.getKey_field(), entity.getId());
                    if (!StringUtils.isEmpty(tmpKeyField)) {
                        cacheKey = String.format("%s%s", cacheSettingModel.getCache_key_pre(), tmpKeyField);
                    } else {
                        //continue;
                        throw new InvokeCommonException(String.format("缓存配置错误，表代码：%s，keyField %s 不存在", tableCode, cacheSettingModel.getKey_field()));
                    }
                } else {
                    cacheKey = String.format("%s%s", cacheSettingModel.getCache_key_pre(), entity.get(cacheSettingModel.getKey_field()).toString());
                }
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

    String getCacheKey(String tableCode, String keyField, Long dataId) {
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
