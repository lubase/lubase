package com.lcp.qibao.invoke;

import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.statistics.StatisticsOption;
import com.lcp.core.service.query.StatisticsCoreService;
import com.lcp.qibao.extend.IGetMainDataService;
import com.lcp.qibao.util.ClientMacro;
import org.springframework.beans.factory.annotation.Autowired;

public class GetStatisticsQueryDemo implements IGetMainDataService {
    @Autowired
    StatisticsCoreService statisticsCoreService;

    @Override
    public String getDescription() {
        return "分组统计demo";
    }

    @Override
    public String getId() {
        return "746686684338327552";
    }

    @Override
    public DbCollection exe(String pageCode, ClientMacro clientMacro) {
        QueryOption queryOption = new QueryOption("v_inte_vehicle");
        queryOption.setTableFilter(new TableFilter("delete_tag", "0"));

        StatisticsOption statisticsOption = new StatisticsOption();
        statisticsOption.setRowField("rdvehicleinfo02");
        statisticsOption.setColumnField("inte_deve_scale");
        statisticsOption.setValueType(1);
        DbCollection collection = statisticsCoreService.queryStatistics(queryOption, statisticsOption);
        return collection;
    }
}
