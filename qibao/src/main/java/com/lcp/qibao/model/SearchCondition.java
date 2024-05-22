package com.lcp.qibao.model;

import lombok.Data;

/**
 * 页面搜索区域匹配条件
 */
@Data
public class SearchCondition {
    //    列的code columnCode 必填，支持多值
//    过滤方式 filterType 必填，默认值：等于
//    排序id orderId 必填
//    是否必填 required 非必填，默认值：否
//    默认值 defaultValue 非必填，默认值：空
//    默认值显示 defaultValueName 下拉或关联字段默认值显示，非必填，默认值：空
//    显示类型 disType 非必填，默认值：空
//    列别名：aliasName
    private String columnCode;
    private ESearchConditionType filterType;
    private Integer orderId;
    private Integer required;
    private String defaultValue;
    private String defaultValueName;
    private String disType;
    private String aliasName;
    private String value;

    public Integer getFilterType() {
        return this.filterType.getType();
    }
    public void setFilterType(Integer value) {
        this.filterType = ESearchConditionType.fromIndexValue(value);
    }
}
