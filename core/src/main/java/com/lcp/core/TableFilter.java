package com.lcp.core;

import com.lcp.core.operate.EOperateMode;
import lombok.Data;

import java.util.List;

/**
 * @author A
 */
@Data
public class TableFilter {
    public TableFilter() {
        //operateMode = EOperateMode.Equals;
    }

    public TableFilter(String filterName, String filterValue) {
        this(filterName, filterValue, EOperateMode.Equals);
    }

    public TableFilter(String filterName, Object filterValue, EOperateMode operateMode) {
        this.filterName = filterName;
        this.filterValue = filterValue;
        this.operateMode = operateMode;
    }

    /**
     * 设置子条件and和or的关系
     */
    private boolean and = true;
    /**
     * 是否取反
     */
    private boolean not;
    /**
     * 表达式左侧的条件，一般是数据库的列名
     */
    private String filterName;
    /**
     * 表达式右侧的值
     */
    private Object filterValue;
    /**
     * 默认值显示名称
     */
    private String filterValueName;

    /**
     * FilterValue 值类型。1：固定值  2：客户端宏变量  3：表单值
     */
    private Integer valueType;
    /**
     * 条件显示名称
     */
    private String filterDisplay;
    /**
     * 条件的逻辑比较符号
     */
    private EOperateMode operateMode;
    /**
     * 子条件
     */
    private List<TableFilter> childFilters;

    private String MainTable;


}
