package com.lcp.core.service.query.impl;

import com.lcp.core.QueryOption;
import com.lcp.core.exception.InvokeCommonException;
import com.lcp.core.mapper.DataAccessMapper;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.QueryJoinCondition;
import com.lcp.core.model.QueryParamEntity;
import com.lcp.core.model.statistics.StatisticsEntity;
import com.lcp.core.model.statistics.StatisticsOption;
import com.lcp.core.multiDataSource.ChangeDataSourceService;
import com.lcp.core.multiDataSource.DBContextHolder;
import com.lcp.core.service.RegisterColumnInfoService;
import com.lcp.core.service.query.StatisticsCoreService;
import com.lcp.core.service.query.TableFilterBuilder;
import com.lcp.coremodel.DbCode;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsCoreServiceImpl implements StatisticsCoreService {
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    DataAccessMapper dataAccessMapper;

    @Autowired
    TableFilterBuilder tableFilterBuilder;

    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @SneakyThrows
    @Override
    public DbCollection queryStatistics(QueryOption queryOption, StatisticsOption statisticsOption) {
        if (StringUtils.isEmpty(queryOption.getFixField())) {
            throw new Exception("no field");
        }
        //强制查询字段为全部小写
        queryOption.setFixField(queryOption.getFixField().toLowerCase());
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryParamEntity queryParam = new QueryParamEntity();
        //1、创建查询字段
        DbTable table = registerColumnInfoService.initTableInfoByTableCode(queryOption.getTableName());
        if (table == null) {
            throw new InvokeCommonException(String.format("表%s不存在", queryOption.getTableName()));
        }
        changeDataSourceService.changeDataSourceByTableCode(table);
        DbField rowField = table.getFieldList().stream().filter(f -> f.getCode().equals(statisticsOption.getRowField())).findFirst().orElse(null);
        DbField columnField = table.getFieldList().stream().filter(f -> f.getCode().equals(statisticsOption.getColumnField())).findFirst().orElse(null);
        if (rowField == null || columnField == null) {
            throw new InvokeCommonException("统计行或统计列设置错误，表中不存在");
        }
        //2、目前只支持单表
        String joinCondition = queryOption.getTableName();
        //3、构建where查询条件
        String where = tableFilterBuilder.parseTableFilterToStr(queryOption.getTableFilter(), table, queryOption.getTableName(), queryParam, queryOption.getRefFields(), queryJoinTables);
        if (StringUtils.isEmpty(where)) {
            where = "1=1";
        }
        //4、执行查询。因为多参数只能传入一个map对象，所以此处需要将sql语句真实的参数放入一个map
        queryParam.setJoinCondition(joinCondition);
        queryParam.setWhere(where);
        queryParam.setStatisticsValueType(statisticsOption.getValueType());
        queryParam.setStatisticsSumField(statisticsOption.getSumField());
        queryParam.setStatisticsRowField(statisticsOption.getRowField());
        queryParam.setStatisticsColumnField(statisticsOption.getColumnField());
        List<StatisticsEntity> statisticsList = new ArrayList<>();
        try {
            if (!table.isMainDatabase()) {
                DBContextHolder.setDataSourceCode(table.getDatabaseId().toString());
            }
            statisticsList = dataAccessMapper.executeQueryGroupStatistics(queryParam);
        } catch (Exception ex) {
            System.out.println("查询数据错误：" + ex.getMessage());
            throw new InvokeCommonException("查询数据出错" + ex.getMessage());
        }
        DbCollection collection = getStatisticsCollection(rowField, columnField, statisticsList);
        return collection;
    }

    private DbCollection getStatisticsCollection(DbField rowField, DbField columnField, List<StatisticsEntity> statisticsList) {
        DbCollection collection = new DbCollection();
        List<DbCode> rCodeData = registerColumnInfoService.getCodeListByTypeId(rowField.getCodeTypeId());
        List<DbCode> cCodeData = registerColumnInfoService.getCodeListByTypeId(columnField.getCodeTypeId());
        DbTable table = new DbTable();
        String colPre = "c_", colRowSum = "c_sum", rowSumCode = "r_sum";
        //固定字段 id
        DbField field = new DbField();
        field.setId("id");
        field.setCode("id");
        field.setVisible(0);
        table.getFieldList().add(field);
        //固定字段 设定的行
        field = new DbField();
        field.setId("row_title");
        field.setCode(field.getId());
        field.setName(String.format("%s\\%s", rowField.getName(), columnField.getName()));
        field.setVisible(2);
        field.setRight(2);
        field.setEleType("1");
        table.getFieldList().add(field);
        field = new DbField();
        field.setId("row_value");
        field.setCode(field.getId());
        table.getFieldList().add(field);
        //遍历列增加字段
        for (DbCode code : cCodeData) {
            field = new DbField();
            field.setId(code.getCode());
            field.setCode(colPre + code.getCode());
            field.setName(code.getName());
            field.setVisible(2);
            field.setRight(2);
            field.setEleType("1");
            table.getFieldList().add(field);
        }
        //增加合计列
        field = new DbField();
        field.setCode(colRowSum);
        field.setId(colRowSum);
        field.setName("总计");
        field.setVisible(2);
        field.setRight(2);
        field.setEleType("1");
        table.getFieldList().add(field);
        collection.setTableInfo(table);
        //遍历行增加数据
        List<DbEntity> listData = new ArrayList<>();
        for (DbCode r : rCodeData) {
            DbEntity entity = collection.newEntity();
            entity.put("row_title", r.getName());
            entity.put("row_value", r.getCode());
            for (DbCode c : cCodeData) {
                entity.put(colPre + c.getCode(), getCellValueByRowAndColumn(statisticsList, r.getCode(), c.getCode()));
            }
            //行合计
            Double rowSum = statisticsList.stream().filter(d -> d.getR() != null && d.getR().equals(r.getCode())).mapToDouble(d -> d.getV()).sum();
            entity.put(colRowSum, rowSum);
            listData.add(entity);
        }
        //增加列合计
        DbEntity entity = collection.newEntity();
        entity.put("row_title", "总计");
        entity.put("row_value", "r_sum");
        for (DbCode c : cCodeData) {
            entity.put(colPre + c.getCode(), statisticsList.stream().filter(d -> d.getC() != null && d.getC().equals(c.getCode())).mapToDouble(d -> d.getV()).sum());
        }
        entity.put(colRowSum, statisticsList.stream().mapToDouble(d -> d.getV()).sum());
        listData.add(entity);
        collection.setData(listData);
        return collection;
    }

    private Double getCellValueByRowAndColumn(List<StatisticsEntity> statisticsList, String r, String c) {
        Double val = null;
        StatisticsEntity entity = statisticsList.stream().filter(d -> d.getR() != null && d.getC() != null
                && d.getR().equals(r) && d.getC().equals(c)).findFirst().orElse(null);
        if (entity != null) {
            val = entity.getV();
        }
        return val;
    }
}
