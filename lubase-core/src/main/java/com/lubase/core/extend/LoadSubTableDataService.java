package com.lubase.core.extend;

import com.lubase.orm.model.DbCollection;

/**
 * 表单子表数据扩展服务
 */
public interface LoadSubTableDataService {
    /**
     * 当前服务id
     *
     * @return
     */
    String getId();

    /**
     * 主表id
     *
     * @return
     */
    String getParentTableId();

    /**
     * 设置功能描述
     *
     * @return
     */
    String getDescription();

    /**
     * 根据主表id获取字表数据
     *
     * @param dataId
     * @return
     */
    DbCollection loadChildTableData(String dataId);
}
