package com.lubase.starter.invoke;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.model.DbCollection;
import com.lubase.core.model.statistics.StatisticsOption;
import com.lubase.core.service.query.StatisticsCoreService;
import com.lubase.starter.extend.IGetMainDataService;
import com.lubase.starter.util.ClientMacro;
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
