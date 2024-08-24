package com.lubase.core.service;

import com.alibaba.fastjson.JSON;
import com.lubase.core.extend.PageTemplateExtendService;
import com.lubase.core.util.ClientMacro;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.statistics.StatisticsOption;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.query.StatisticsCoreService;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RenderPageTemplate1160Service implements PageTemplateExtendService {

    @Autowired
    StatisticsCoreService statisticsCoreService;

    @Override
    public void beforeExecuteQuery(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {

    }

    @Override
    public void beforeReturnMainData(DbEntity pageEntity, DbCollection collection, ClientMacro clientMacro) {

    }

    @SneakyThrows
    @Override
    public DbCollection executePageTemplate(DataAccess dataAccess, DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {
        String settingStr = TypeConverterUtils.object2String(pageEntity.get("statistics_setting"), "");
        if (StringUtils.isEmpty(settingStr)) {
            throw new WarnCommonException("图表配置参数错误，请检查");
        }
        StatisticsOption statisticsOption = null;
        try {
            statisticsOption = JSON.parseObject(settingStr, StatisticsOption.class);
        } catch (Exception exception) {
            throw new WarnCommonException("图表配置参数错误，请检查");
        }
        if (statisticsOption == null || StringUtils.isEmpty(statisticsOption.getRowField())) {
            throw new WarnCommonException("图表配置参数错误，请检查");
        }
        return statisticsCoreService.queryStatistics(queryOption, statisticsOption);
    }

    @Override
    public String getTemplateCode() {
        // 统计分析模板
        return "1160";
    }
}
