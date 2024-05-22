package com.lcp.settingcache.service;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.SsCacheEntity;

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
