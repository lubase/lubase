package com.lcp.core.extend;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbTable;

/**
 * 表触发器接口
 *
 * @author A
 */
public interface ITableTrigger {

    /**
     * 表代码
     *
     * @return
     */
    String getTriggerTableCode();

    /**
     * 返回触发器的名字
     *
     * @return
     */
    String getTriggerName();

    /**
     * 数据新增是否触发此触发器
     *
     * @return
     */
    default Boolean isAdd() {
        return false;
    }

    /**
     * 数据修改是否触发此触发器
     *
     * @return
     */
    default Boolean isEdit() {
        return false;
    }

    /**
     * 数据删除是否触发此触发器
     *
     * @return
     */
    default Boolean isDelete() {
        return false;
    }

    default Boolean GlobalTriggerFilter(DbTable tableInfo) {
        return false;
    }

    /**
     * 更新前，对数据进行验证
     *
     * @param tableInfo
     * @param entity
     * @param isServer
     * @return
     */
    default Boolean beforeValidate(DbTable tableInfo, DbEntity entity, Boolean isServer) throws Exception {
        return true;
    }

    /**
     * 更新后的事件
     *
     * @param tableInfo
     * @param entity
     * @param isServer
     */
    default void afterUpdate(DbTable tableInfo, DbEntity entity, Boolean isServer) throws Exception {

    }

    /**
     * 事务开启前，对数据进行验证
     *
     * @param tableInfo
     * @param entity
     * @param isServer
     * @return
     */
    default boolean beforeTransactionValidate(DbTable tableInfo, DbEntity entity, Boolean isServer) throws Exception {
        return true;
    }

    /**
     * 事务提交前，对后续业务的操作
     *
     * @param tableInfo
     * @param entity
     * @param isServer
     */
    default void afterTransactionUpdate(DbTable tableInfo, DbEntity entity, Boolean isServer) throws Exception {
    }
}
