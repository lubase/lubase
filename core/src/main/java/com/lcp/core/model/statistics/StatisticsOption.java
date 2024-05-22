package com.lcp.core.model.statistics;

import lombok.Data;

/**
 * 统计配置选项
 */
@Data
public class StatisticsOption {
    public StatisticsOption() {
        this.valueType = 1;
    }

    private String rowField;
    private String columnField;
    private String sumField;
    /**
     * 1:数量  2：求和。 默认求数量
     */
    private int valueType;
    private Boolean enableRowSum;
    private Boolean enableColumnSum;
}
