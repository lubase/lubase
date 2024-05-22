package com.lcp.qibao.extend;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.qibao.util.ClientMacro;

/**
 * 表单关联字段数据源获取服务
 */
public interface LookupColumnDataService {
    /**
     * 服务id
     *
     * @return
     */
    String getServiceId();

    /**
     * 列id
     *
     * @return
     */
    String getColumnId();

    /**
     * 服务描述
     *
     * @return
     */
    String getDescription();

    /**
     * 关联表的标识列，默认为id，不可修改
     *
     * @return
     */
    default String getTableKey() {
        return "id";
    }

    /**
     * 获取显示列
     *
     * @return
     */
    String getDisplayCol();

    /**
     * 扩展显示列
     *
     * @return
     */
    String getExtendCol();

    /**
     * 根据客户端条件返回所需的数据
     *
     * @param formData
     * @param clientQuery
     * @return
     */
    DbCollection getColLookupData(ClientMacro clientMacro, String formData, QueryOption clientQuery);
}
