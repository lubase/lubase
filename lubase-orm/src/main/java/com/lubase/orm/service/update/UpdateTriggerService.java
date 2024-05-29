package com.lubase.orm.service.update;

import com.lubase.orm.model.DbCollection;

/**
 * dataAccess  update 方法通用的更新触发器
 */
public interface UpdateTriggerService {
    /**
     * 更新前数据处理事件
     *
     * @param collection
     */
    default void beforeUpdate(DbCollection collection) {
    }

    /**
     * 更新事务提交后
     *
     * @param collection
     */
    default void afterUpdate(DbCollection collection, Integer updateRowCount) {
    }
}
