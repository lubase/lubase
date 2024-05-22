package com.lcp.core.service.query;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.statistics.StatisticsOption;

public interface StatisticsCoreService {

    /**
     * 简单的分组统计功能
     *
     * @param queryOption
     * @param statisticsOption
     * @return
     */
    DbCollection queryStatistics(QueryOption queryOption, StatisticsOption statisticsOption);
}
