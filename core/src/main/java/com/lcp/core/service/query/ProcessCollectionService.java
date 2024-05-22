package com.lcp.core.service.query;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbTable;

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
