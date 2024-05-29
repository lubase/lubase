package com.lubase.wfengine.model;

import lombok.Data;

@Data
public class WfTaskFieldModel {
    private String id;
    private Integer isNull;
    private Integer fieldAccess;
    /**
     * 默认字段是否允许为空
     */
    private Integer _default_IsNull;
    /**
     * 默认字段可编辑属性
     */
    private Integer _default_fieldAccess;
    private String name;
    /**
     * 1: 字段  2：子表列表
     */
    private Integer elementType;
}
