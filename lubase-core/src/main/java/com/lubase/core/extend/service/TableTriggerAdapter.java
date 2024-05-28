package com.lubase.core.extend.service;

import com.lubase.core.extend.ITableTrigger;
import com.lubase.model.DbTable;

import java.util.List;

public interface TableTriggerAdapter {
    /**
     * 根据表code 表触发器列表
     *
     * @param tableInfo
     * @return
     */
    List<ITableTrigger> getTableTriggerList(DbTable tableInfo);

    /**
     * 获取所有表触发器
     *
     * @return
     */
    List<ITableTrigger> getTableTriggerList();
}
