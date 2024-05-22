package com.lcp.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
import com.lcp.coremodel.EDBEntityState;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;


/**
 * @author A
 */
@Data
public class DbCollection {
    /**
     * 设置数据集合
     */
    private List<DbEntity> data;
    /**
     * 表结构信息
     */
    public DbTable tableInfo;
    /**
     * 是否启用表触发器。默认：启用
     */
    @JsonIgnore
    private boolean enableTableTrigger = true;
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean isServer;

    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 分页大小
     */
    private int pageSize;
    /**
     * 当前集合返回的记录数
     */
    @Setter(AccessLevel.NONE)
    private int count;

    /**
     * 对象状态，用于在update、query方法中各个事件进行参数传递
     */
    @JsonIgnore
    private Object objState;

    /**
     * 个性化配置
     */
    private String personalization;

    public DbCollection() {
        this.isServer = true;
    }

    @JsonIgnore
    public String getTableName() {
        if (tableInfo != null) {
            return tableInfo.getCode();
        } else {
            return null;
        }
    }

    /**
     * 设置对象为客户端模式，客户端模式对严格按照字段权限进行更新
     */
    public void setClientMode() {
        this.isServer = false;
    }

    private int getCount() {
        if (null == data) {
            return 0;
        } else {
            return this.data.size();
        }
    }

    /**
     * 获取新增数据
     *
     * @return
     */
    public DbEntity newEntity() {
        DbEntity entity = new DbEntity();
        entity.setState(EDBEntityState.Added);
        if (null != tableInfo) {
            for (DbField field : tableInfo.getFieldList()) {
                entity.put(field.getCode(), "");
            }
        }
        return entity;
    }

    /**
     * 获取强类型的数据
     *
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T extends DbEntity> List<T> getGenericData(Class<T> tClass) {
        List<T> list = new ArrayList<>();
        if (this.data == null || this.data.size() == 0) {
            return list;
        }
        for (DbEntity entity : this.data) {
            T newEntity = tClass.getDeclaredConstructor().newInstance();
            newEntity.cloneFromNewEntity(entity);
            list.add(newEntity);
        }
        return list;
    }

    /**
     * 将强类型的对象转换成标准的对象
     *
     * @param list
     * @param <T>
     */
    public <T extends DbEntity> void setGenericData(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.data = new ArrayList<>();
        for (DbEntity entity : list) {
            DbEntity newEntity = new DbEntity();
            newEntity.cloneFromNewEntity(entity);
            this.data.add(newEntity);
        }
    }
}
