package com.lubase.core.model.customForm;

import lombok.Data;

/**
 * 用于客户端请求lookup字段的关联信息
 */
@Data
public class ColumnLookupParamModel {
    /**
     * 按钮或者页面的功能代码
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
     * 分页查询条件
     */
    private String queryParam;
    /**
     * 搜索区域条件
     */
    private String searchParam;

    /**
     * 客户端参数
     */
    private String clientMacro;
    /**
     * 表单值
     */
    private String formData;

    /**
     * 是否只获取配置信息
     */
    private Integer onlyConfig;
}
