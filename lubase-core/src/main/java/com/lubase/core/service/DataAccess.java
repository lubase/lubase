package com.lubase.core.service;

import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.model.DbCode;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author A
 */
public interface DataAccess {


    /**
     * 标砖的query查询，默认分页
     *
     * @param queryOption 返回数据和表结构信息
     * @return
     */
    DbCollection query(QueryOption queryOption);

    /**
     * 不分页查询所有数据
     *
     * @param queryOption
     * @return
     */
    DbCollection queryAllData(QueryOption queryOption);

    /**
     * 不查询关联字段
     *
     * @param queryOption
     * @return
     */
    default DbCollection queryNoneLookupField(QueryOption queryOption) {
        queryOption.setBuildLookupField(false);
        return query(queryOption);
    }

    /**
     * 只查询表结构
     *
     * @param queryOption
     * @return
     */
    DbCollection queryFieldList(QueryOption queryOption);

    /**
     * <p>
     * 根据表名称，ID获取记录
     * </p>
     *
     * @param tableName 表名称
     * @param id        记录id
     * @return DbCollection  数据状态是Unchanged
     * @since 2022/4/3 0003
     */
    @SneakyThrows
    DbCollection queryById(String tableName, Long id);

    @SneakyThrows
    DbCollection queryById(String tableName, Long id, String fixField);

    /**
     * 根据表代码获取表结构信息
     *
     * @param tableName
     * @return
     */
    DbCollection getEmptyData(String tableName);

    /**
     * 根据表id获取结构信息
     *
     * @param tableId
     * @return
     */
    DbCollection getEmptyDataByTableId(Long tableId);

    /**
     * 根据表代码获取表信息
     *
     * @param tableCode
     * @return
     */
    DbTable initTableInfoByTableCode(String tableCode);

    /**
     * 根据表名字获取系统默认的注册列信息
     *
     * @param tableId
     * @return
     */
    DbTable initTableInfoByTableId(Long tableId);

    DbField getDbFieldByColumnId(Long columnId);

    /**
     * 数据更新
     *
     * @param collection
     * @return
     */
    @SneakyThrows
    Integer update(DbCollection collection);

    /**
     * 获取受控表
     *
     * @return
     */
    List<String> getControlledTableList();

    /**
     * 通过存储过程获取数据列表
     *
     * @param procName
     * @param p1
     * @return
     */
    List<DbEntity> procGetDbEntityList(String procName, String... p1);

    /**
     * 通过存储过程获取id列表
     *
     * @param procName
     * @param p1
     * @return
     */
    List<String> procGetStringList(String procName, String... p1);

    /**
     * 获取代码表数据
     * @param codeTypeId
     * @return
     */
    List<DbCode> getCodeListByTypeId(String codeTypeId);
}
