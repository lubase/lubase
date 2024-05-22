package com.lcp.qibao.model.customForm;

import com.lcp.core.model.DbCollection;
import com.lcp.qibao.model.SearchCondition;
import lombok.Data;

import java.util.List;

@Data
public class ColumnLookupInfoVO {
    /**
     * 列表信息
     */
    private DbCollection dbCollection;

    /**
     * 关联字段
     */
    private String tableKey;
    /**
     * 显示字段
     */
    private String displayCol;
    /**
     * 搜索区域的列，如果有个多个中间用逗号隔开
     */
    private String searchCols;
    /**
     * 搜索条件
     */
    private List<SearchCondition> filter;
    /**
     * 表格渲染配置
     */
    @Deprecated
    private String tableInfo;
    /**
     * 表格渲染配置
     */
    private String gridInfo;
}
