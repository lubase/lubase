package com.lcp.core.model;

import lombok.Data;

/**
 * 解析规则
 */
@Data
public class RelateUpdateModel {

    /**
     * 目标表的列代码
     */
    String targetColCode;
    /**
     * 1：当前表列的代码   2:固定值
     */
    Integer valueType;
    /**
     * 根据valueType 决定此值如何解析
     */
    String updateValue;
}
