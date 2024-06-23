package com.lubase.wfengine.model;

import lombok.Data;

@Data
public class WfTaskFieldModel {
    private String id;
    private String name;
    /**
     * 1: 字段  2：子表列表
     */
    private Integer elementType;

    /***
     * 是否只读
     */
    private Integer readonly;
    /**
     * 是否必填
     */
    private Integer required;
    // 是否隐藏字段
    private Integer hidden;
    private Integer _default_required;
    private Integer _default_readonly;
}
