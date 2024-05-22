package com.lcp.core.model;

import lombok.Data;

/**
 * 关联表条件
 *
 * @author A
 */
@Data
public class QueryJoinCondition {
    private String tableAlias;
    private String condition;
}
