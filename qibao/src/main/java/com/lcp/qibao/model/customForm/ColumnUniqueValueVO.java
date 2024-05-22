package com.lcp.qibao.model.customForm;

import lombok.Data;

@Data
public class ColumnUniqueValueVO {
    /**
     * 页面id
     */
    private String funcCode;
    /**
     * 表单ID，通过表单关联到功能，进行权限校验
     */
    private String formId;
    /**
     * 列的ID
     */
    private String columnId;

    /**
     * 列值
     */
    private String columnValue;

    /**
     * 当前数据id
     */
    private String dataId;
}
