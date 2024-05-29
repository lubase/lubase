package com.lubase.orm.model;

import lombok.Data;

/**
 * 用于 dataAcess.query 的参数查询
 *
 * @author A
 */
@Data
public class QueryParamEntity extends SqlEntity {
    private String queryFields;
    private String joinCondition;
    private String where;
    private String sortFields;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer queryMode;

    private Integer statisticsValueType;
    private String statisticsRowField;
    private String statisticsColumnField;
    private String statisticsSumField;

    private String databaseType;
}
