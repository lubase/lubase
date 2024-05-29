package com.lubase.orm.service.query;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.statistics.StatisticsOption;

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
