package com.lcp.core.model;

/**
 * filer lookup的表 中解析sql语句的模式
 *
 * @author A
 */

public enum ETableFilter2SqlMode {
    /**
     *
     */
    None,
    /**
     * 常规的条件比较模式 left join模式
     */
    Compare,
    /**
     * 生成exist 语句
     */
    Exists
}
