package com.lubase.cache.service;

import com.lubase.model.DbEntity;
import com.lubase.model.SsCacheEntity;

import java.util.List;

public interface UpdateSettingDataService {


    /**
     * 获取所有启用缓存设置的表列表
     *
     * @return
     */
    List<SsCacheEntity> getTableCacheSettingList();

    /**
     * 所有的级联更新表设置
     *
     * @return
     */
    List<DbEntity> getTableRelateSettingList();
}
