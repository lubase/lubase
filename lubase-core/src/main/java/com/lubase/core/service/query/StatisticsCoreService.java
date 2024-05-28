package com.lubase.core.service.query;

import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.model.statistics.StatisticsOption;

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
