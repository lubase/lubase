package com.lubase.orm.extend;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.model.DbEntity;

public interface IColumnRemoteService {
    /**
     * 设置方法描述
     *
     * @return
     */
    String getDescription();

    /**
     * 服务唯一标识
     *
     * @return
     */
    String getId();

    /**
     * 显示字段
     */
    String displayCol();

    /**
     * 用于弹窗显示类型的搜索列
     *
     * @return
     */
    default String searchCols() {
        return displayCol();
    }

    /**
     * 查询的key
     *
     * @return
     */
    String tableKey();

    /**
     * 根据key获取缓存数据
     *
     * @param key
     * @return
     */
    DbEntity getCacheDataByKey(String key);

    /**
     * 根据检索条件数据列表，用于支持弹窗选人
     *
     * @param clientQuery
     * @param clientMacroStr 客户端宏变量字符串
     * @return
     */
    default DbCollection getDataByFilter(QueryOption clientQuery, String clientMacroStr) {
        return getAllData();
    }

    /**
     * 查询所有用户信息，禁止在事务中使用此方法
     *
     * @return
     */
    DbCollection getAllData();

    /**
     * 获取默认的对象
     *
     * @param key
     * @return
     */
    default DbEntity getDefaultEntity(String key) {
        DbEntity entity = new DbEntity();
        entity.put(tableKey(), key);
        entity.put(displayCol(), key);
        return entity;
    }
}
