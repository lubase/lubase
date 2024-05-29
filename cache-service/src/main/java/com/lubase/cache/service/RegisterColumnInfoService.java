package com.lubase.cache.service;


import com.lubase.model.DbCode;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author A
 * @since 2021-12-19
 */
public interface RegisterColumnInfoService {
    /**
     * 根据表名字获取表信息
     *
     * @param tableId
     * @return
     */
    DbTable initTableInfoByTableId(Long tableId);

    /**
     * 根据表代码获取表结构
     *
     * @param tableCode
     * @return
     */
    DbTable initTableInfoByTableCode(String tableCode);

    /**
     * 根据表代码获取表code
     *
     * @param tableCode
     * @return
     */
    String getTableIdByTableCode(String tableCode);

    /**
     * 根据表id获取表code
     *
     * @param tableId
     * @return
     */
    String getTableCodeByTableId(Long tableId);

    /**
     * 根据列id 获取 列信息
     *
     * @param columnId
     * @return
     */
    DbField getColumnInfoByColumnId(Long columnId);

    /**
     * 根据表id获取注册列信息
     *
     * @param tableId
     * @return
     */
    List<DbField> getColumnsByTableId(Long tableId);

    /**
     * 获取受控表清单
     *
     * @return
     */
    List<String> getControlledTableList();

    /**
     * 获取代码表
     *
     * @param codeTypeId
     * @return
     */
    List<DbCode> getCodeListByTypeId(String codeTypeId);

}
