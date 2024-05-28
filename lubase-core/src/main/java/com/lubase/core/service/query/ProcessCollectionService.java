package com.lubase.core.service.query;

import com.lubase.model.DbEntity;
import com.lubase.model.DbTable;

import java.util.List;

/**
 * 处理 dbCollection对象中 代码表、关联字段等信息
 */
public interface ProcessCollectionService {
    /**
     * 处理特殊显示的字段
     *
     * @param entityList
     * @param tableInfo
     */
    void processDataList(List<DbEntity> entityList, DbTable tableInfo);
}
