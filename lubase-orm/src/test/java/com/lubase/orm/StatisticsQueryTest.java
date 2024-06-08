package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.statistics.StatisticsOption;
import com.lubase.orm.service.query.StatisticsCoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StatisticsQueryTest {

    @Autowired
    StatisticsCoreService statisticsCoreService;

    @Test
    void testMainDB() {
        QueryOption queryOption = new QueryOption("dm_column");
        StatisticsOption statisticsOption = new StatisticsOption();
        statisticsOption.setRowField("data_type");
        statisticsOption.setColumnField("ele_type");
        statisticsOption.setValueType(1);
        DbCollection collection = statisticsCoreService.queryStatistics(queryOption, statisticsOption);
        System.out.println(JSON.toJSON(collection));
    }

    @Test
    void testOtherDB() {
        QueryOption queryOption = new QueryOption("v_inte_vehicle");
        queryOption.setTableFilter(new TableFilter("delete_tag", "0"));
        StatisticsOption statisticsOption = new StatisticsOption();
        statisticsOption.setRowField("brand");
        statisticsOption.setColumnField("inte_deve_scale");
        statisticsOption.setValueType(1);
        DbCollection collection = statisticsCoreService.queryStatistics(queryOption, statisticsOption);
        System.out.println(JSON.toJSON(collection));
    }

    @Test
    void testGetInfoList() {

    }
}
