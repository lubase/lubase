package com.lubase.orm.service.query.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.mapper.DataAccessMapper;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.QueryJoinCondition;
import com.lubase.orm.model.QueryParamEntity;
import com.lubase.orm.model.statistics.StatisticsEntity;
import com.lubase.orm.model.statistics.StatisticsOption;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.service.query.StatisticsCoreService;
import com.lubase.orm.service.query.TableFilterBuilder;
import com.lubase.model.DbCode;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
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
        if (rowField == null) {
            throw new InvokeCommonException("统计行设置错误，表中不存在");
        }
        DbField columnField = null;
        if (!StringUtils.isEmpty(statisticsOption.getColumnField())) {
            columnField = table.getFieldList().stream().filter(f -> f.getCode().equals(statisticsOption.getColumnField())).findFirst().orElse(null);
            if (columnField == null) {
                throw new InvokeCommonException("统计列设置错误，表中不存在");
            }
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
        if (rCodeData != null && !rCodeData.isEmpty()) {
            rCodeData.sort(Comparator.comparingInt(DbCode::getOrder_id));
        } else {
            rCodeData = new ArrayList<>();
            // 从  statisticsList 获取列
            for (StatisticsEntity entity : statisticsList) {
                if (rCodeData.stream().noneMatch(dbCode -> dbCode.getCode().equals(entity.getR()))) {
                    DbCode dbCode = new DbCode();
                    dbCode.setCode(entity.getR());
                    dbCode.setName(entity.getR());
                    rCodeData.add(dbCode);
                }
            }
        }

        List<DbCode> cCodeData = new ArrayList<>();
        if (columnField != null) {
            cCodeData = registerColumnInfoService.getCodeListByTypeId(columnField.getCodeTypeId());
        }
        if (cCodeData == null) {
            cCodeData = new ArrayList<>();
        }
        cCodeData.sort(Comparator.comparingInt(DbCode::getOrder_id));
        DbTable table = new DbTable();
        String colPre = "c_", colRowSum = "c_sum", rowSumCode = "r_sum";
        //固定字段 id
        Integer columnOrder = 1;
        DbField field = new DbField();
        field.setId("id");
        field.setCode("id");
        field.setVisible(0);
        field.setOrderId(columnOrder++);
        table.getFieldList().add(field);
        //固定字段 设定的行
        field = new DbField();
        field.setId("row_title");
        field.setCode(field.getId());
        if (columnField != null) {
            field.setName(String.format("%s\\%s", rowField.getName(), columnField.getName()));
        } else {
            field.setName(rowField.getName());
        }
        field.setVisible(2);
        field.setRight(2);
        field.setEleType("1");
        field.setOrderId(columnOrder++);
        table.getFieldList().add(field);
        field = new DbField();
        field.setId("row_value");
        field.setCode(field.getId());
        field.setOrderId(columnOrder++);
        table.getFieldList().add(field);
        //遍历列增加字段
        for (DbCode code : cCodeData) {
            field = new DbField();
            field.setId(code.getCode());
            field.setCode(colPre + code.getCode());
            field.setOrderId(columnOrder++);
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
        field.setOrderId(columnOrder);
        table.getFieldList().add(field);
        collection.setTableInfo(table);
        //遍历行增加数据
        List<DbEntity> listData = new ArrayList<>();
        for (DbCode r : rCodeData) {
            DbEntity entity = collection.newEntity();
            entity.put("row_title", r.getName());
            entity.put("row_value", r.getCode());
            if (!cCodeData.isEmpty()) {
                for (DbCode c : cCodeData) {
                    entity.put(colPre + c.getCode(), getCellValueByRowAndColumn(statisticsList, r.getCode(), c.getCode()));
                }
            }
            //行合计
            Double rowSum = statisticsList.stream().filter(d -> d.getR() != null && d.getR().equals(r.getCode())).mapToDouble(StatisticsEntity::getV).sum();
            entity.put(colRowSum, rowSum);
            listData.add(entity);
        }
        //增加列合计
        DbEntity entity = collection.newEntity();
        entity.put("row_title", "总计");
        entity.put("row_value", "r_sum");
        for (DbCode c : cCodeData) {
            entity.put(colPre + c.getCode(), statisticsList.stream().filter(d -> d.getC() != null && d.getC().equals(c.getCode())).mapToDouble(StatisticsEntity::getV).sum());
        }
        entity.put(colRowSum, statisticsList.stream().mapToDouble(StatisticsEntity::getV).sum());
        listData.add(entity);
        collection.setData(listData);
        return collection;
    }

    private Double getCellValueByRowAndColumn(List<StatisticsEntity> statisticsList, String r, String c) {
        Double val = 0D;
        StatisticsEntity entity = statisticsList.stream().filter(d -> d.getR() != null && d.getC() != null
                && d.getR().equals(r) && d.getC().equals(c)).findFirst().orElse(null);
        if (entity != null) {
            val = entity.getV();
        }
        return val;
    }
}
