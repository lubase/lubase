package com.lubase.orm.service.query.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.constant.CommonConst;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.mapper.DataAccessMapper;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LookupMode;
import com.lubase.orm.model.QueryJoinCondition;
import com.lubase.orm.model.QueryParamEntity;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.service.query.DataAccessColumnRightService;
import com.lubase.orm.service.query.DataAccessQueryCoreService;
import com.lubase.orm.service.query.LookupFieldParseService;
import com.lubase.orm.service.query.TableFilterBuilder;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import com.lubase.orm.util.SpringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Service
public class DataAccessQueryCoreServiceImpl implements DataAccessQueryCoreService {
    @Autowired
    DataAccessMapper dataAccessMapper;
    @Autowired
    TableFilterBuilder tableFilterBuilder;

    @Autowired
    LookupFieldParseService lookupFieldParseService;

    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    List<DataAccessColumnRightService> columnRightServiceList;

    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @SneakyThrows
    @Override
    public DbCollection query(QueryOption queryOption, Boolean onlyQueryFieldList) {
        if (StringUtils.isEmpty(queryOption.getFixField())) {
            throw new Exception("no field");
        }
        //强制查询字段为全部小写
        queryOption.setFixField(queryOption.getFixField().toLowerCase());
        DbCollection collection = new DbCollection();
        //Map<String, String> queryJoinTables = new HashMap<>();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryParamEntity queryParam = new QueryParamEntity();
        //1、创建查询字段
        DbTable table = registerColumnInfoService.initTableInfoByTableCode(queryOption.getTableName());
        if (table == null) {
            throw new InvokeCommonException(String.format("表%s不存在", queryOption.getTableName()));
        }
        //优化后的切库逻辑：在从缓存获取到表结构后再按需切库
        changeDataSourceService.changeDataSourceByTableCode(table);
        String queryFields = generateFields(queryOption, queryJoinTables, collection);
        //2、创建表关联条件
        String joinCondition = generateJoinCondition(queryOption.getTableName(), queryJoinTables);
        //3、构建where查询条件
        String where = tableFilterBuilder.parseTableFilterToStr(queryOption.getTableFilter(), table, queryOption.getTableName(), queryParam, queryOption.getRefFields(), queryJoinTables);
        if (StringUtils.isEmpty(where)) {
            where = "1=1";
        }
        //4、排序字段
        String sortFields = "";
        if (queryOption.getSortFieldsList().size() > 0) {
            sortFields = String.join(",", queryOption.getSortFieldsList());
        } else if (!StringUtils.isEmpty(queryOption.getSortField())) {
            sortFields = queryOption.getSortField();
        }
        if (onlyQueryFieldList) {
            return collection;
        }
        queryParam.setDatabaseType(table.getDatabaseType());
        //5、执行查询。因为多参数只能传入一个map对象，所以此处需要将sql语句真实的参数放入一个map
        queryParam.setQueryFields(queryFields);
        queryParam.setJoinCondition(joinCondition);
        queryParam.setWhere(where);
        queryParam.setSortFields(sortFields);
        queryParam.setPageIndex(queryOption.getPageIndex());
        queryParam.setPageSize(queryOption.getPageSize());
        queryParam.setQueryMode(queryOption.getQueryMode());
        try {
            if (!table.isMainDatabase()) {
                DBContextHolder.setDataSourceCode(table.getDatabaseId().toString());
            }
            collection.setData(dataAccessMapper.executeQueryList(queryParam));
            if (queryOption.getPageIndex() > 0 && queryOption.getPageSize() > 0) {
                //分页查询才查询总记录数
                if (!table.isMainDatabase()) {
                    DBContextHolder.setDataSourceCode(table.getDatabaseId().toString());
                }
                collection.setTotalCount(dataAccessMapper.executeQueryCount(queryParam));
                collection.setPageSize(queryOption.getPageSize());
            } else {
                collection.setTotalCount(collection.getData().size());
                collection.setPageSize(0);
            }
        } catch (BadSqlGrammarException badSqlGrammarException) {
            System.out.println("查询数据错误：" + badSqlGrammarException.getMessage());
            String monster = "";
            try {
                monster = dataAccessMapper.getMonster();
            } catch (Exception e) {
                log.error("getMonster 执行失败，未抓取到 ", e);
            }
            DataSource dataSource = SpringUtil.getApplicationContext().getBean(DataSource.class);
            String currentDb = dataSource.getConnection().getCatalog();
            String msg = String.format("当前数据库连接：%s，monster is %s，错误：%s", currentDb, monster, badSqlGrammarException.getMessage());
            log.error(msg);
            System.out.println(msg);
            throw badSqlGrammarException;
        } catch (Exception ex) {
            System.out.println("查询数据错误：" + ex.getMessage());
            throw ex;
        }
        return collection;
    }

    @SneakyThrows
    private String generateFields(QueryOption queryOption, List<QueryJoinCondition> queryJoinTables, DbCollection collection) {
        DbTable userTable = getUserTableInfo(queryOption, queryJoinTables);
        collection.setTableInfo(userTable);
        //获取场景特定查询字段
        List<DbField> canSelectFieldList = userTable.getFieldList();
        //拼凑查询字段
        Map<String, String> queryFields = new HashMap<>();
        for (DbField colInfo : canSelectFieldList) {
            LookupMode colLookupMode = LookupMode.FromJsonStr(colInfo.getLookup());
            if (colInfo.getTableId().equals(userTable.getId())) {
                //主表查询字段
                queryFields.put(colInfo.getId(), getSqlObj(userTable.getDatabaseType(), colInfo.getTableCode()) + "." + getSqlObj(userTable.getDatabaseType(), colInfo.getCode()));
            } else {
                //关联表查询字段
                String tmpColCode = getSqlObj(userTable.getDatabaseType(), colInfo.getTableCode()) + "." + getSqlObj(userTable.getDatabaseType(), colInfo.getCode()) + " AS " + getSqlObj(userTable.getDatabaseType(),
                        colInfo.getTableCode() + CommonConst.REF_FIELD_SEPARATOR + colInfo.getCode());
                if (queryFields.containsKey(colInfo.getId())) {
                    queryFields.put(colInfo.getId(), String.format("%s,%s", queryFields.get(colInfo.getId()), tmpColCode));

                } else {
                    queryFields.put(colInfo.getId(), tmpColCode);
                }
            }
            //判断不处理情况。 仅处理 eleType=7 and  isMultivalued=0
            if (!queryOption.isBuildLookupField() ||
                    colLookupMode == null ||
                    !colInfo.getEleType().equals("7") ||
                    (colInfo.getIsMultivalued() != null && colInfo.getIsMultivalued() == 1) ||
                    colLookupMode.getTableKey().equals(colLookupMode.getDisplayCol())) //关联字段与显示字段一致
            {
                continue;
            }
            //取关联表的信息
            DbTable lookTableInfo = registerColumnInfoService.initTableInfoByTableCode(colLookupMode.getTableCode());
            if (lookTableInfo == null) {
                throw new WarnCommonException("字段" + colInfo.getCode() + "关联信息设置不正确，请检查");
            }
            DbField colKey = lookTableInfo.firstOrDefault(f -> f.getCode().equals(colLookupMode.getTableKey()) && f.getTableCode().equals(colLookupMode.getTableCode()));
            DbField colDisplay = lookTableInfo.firstOrDefault(f -> f.getCode().equals(colLookupMode.getDisplayCol()) && f.getTableCode().equals(colLookupMode.getTableCode()));
            if (colKey == null) {
                //TODO:未找到关联字段，异常记录日志信息
            }
            if (colDisplay == null) {
                queryFields.put(colInfo.getId(), String.format("%s,%s", queryFields.get(colInfo.getId()), "NULL AS " + getSqlObj(userTable.getDatabaseType(), colInfo.getCode() + "NAME")));
            } else {
                // LookUp 字段单选字段被查询, 则取出显示的值
                String tableAlias = colInfo.getCode();
                if (queryOption.getTableName().compareToIgnoreCase(colInfo.getTableCode()) != 0) {
                    tableAlias = String.format("%s%s%s", colInfo.getTableCode(), CommonConst.REF_FIELD_SEPARATOR, tableAlias);
                }
                String joinTableKey = getSqlObj(userTable.getDatabaseType(), lookTableInfo.getCode()) + " AS " + getSqlObj(userTable.getDatabaseType(), tableAlias);
                boolean containsJoinTable = false;
                for (QueryJoinCondition queryJoinCondition : queryJoinTables) {
                    if (queryJoinCondition.getTableAlias().equals(joinTableKey)) {
                        containsJoinTable = true;
                        break;
                    }
                }
                if (!containsJoinTable) {
                    QueryJoinCondition queryJoinCondition = new QueryJoinCondition();
                    queryJoinCondition.setTableAlias(joinTableKey);
                    queryJoinCondition.setCondition(String.format("%s.%s = %s.%s", getSqlObj(userTable.getDatabaseType(), colInfo.getTableCode()), getSqlObj(userTable.getDatabaseType(), colInfo.getCode()),
                            getSqlObj(userTable.getDatabaseType(), tableAlias), getSqlObj(userTable.getDatabaseType(), colLookupMode.getTableKey())));
                    queryJoinTables.add(queryJoinCondition);
                }
                boolean isMainTable = colInfo.getTableId().equals(userTable.getId());
                String colLookupDisplay = (isMainTable ? "" : (colInfo.getTableCode() + CommonConst.REF_FIELD_SEPARATOR)) + colInfo.getCode() + "NAME";
                queryFields.put(colInfo.getId(), String.format("%s,%s", queryFields.get(colInfo.getId()), getLookupDisplayField(userTable.getDatabaseType(), colInfo, colLookupMode, isMainTable, colLookupDisplay)));
            }
        }
        // 此代码应该无用，因为在parsetTableField 已经为字段设置了别名
        // 此段代码有用，对于关联表的字段名是需要进行重命名的 ss 0828
        //设置关联表字段别名，避免列名重复
        for (DbField colInfo : userTable.getFieldList()) {
            if (!Objects.equals(colInfo.getTableCode(), userTable.getCode())) {
                colInfo.setCode(String.format("%s%s%s", colInfo.getTableCode(), CommonConst.REF_FIELD_SEPARATOR, colInfo.getCode()));
            }
        }
        StringBuilder sbCols = new StringBuilder();
        for (String item : queryFields.values()) {
            sbCols.append("," + item);
        }
        return sbCols.substring(1);
    }

    @SneakyThrows
    private DbTable getUserTableInfo(QueryOption queryOption, List<QueryJoinCondition> queryJoinTables) {
        DbTable table = registerColumnInfoService.initTableInfoByTableCode(queryOption.getTableName());
        //手工设置的reffield lookup优先级高于DB的设置
        //SetQueryRef(queryOption.RefFields, table);
        List<DbField> fixFieldList = new ArrayList<>();
        //处理固定字段
        if (!queryOption.getFixField().equals("*")) {
            //默认增加主键查询
            DbField colId = table.firstOrDefault(f -> f.getTableId().equals(table.getId()) && f.isPrimaryKey());
            fixFieldList.add(colId);
            for (String fField : queryOption.getFixField().split("\\,")) {
                if (fField.contains(".")) {
                    DbField exists = getFieldByCode(fixFieldList, fField);
                    if (exists != null) {
                        continue;
                    }
                    DbField field = lookupFieldParseService.parseLookupField(fField, table, queryOption.getRefFields(), queryJoinTables);
                    if (field == null) {
                        throw new Exception("固定字段:" + fField + " 解析失败");
                    }
                    //设置ref信息
                    if (queryOption.getRefFields() != null && queryOption.getRefFields().containsKey(String.format("%s.%s", field.getTableCode(), field.getCode()))) {
                        //todo:设置lookupmode信息
                        //field.setLookup("");
                    }
                    fixFieldList.add(field);
                } else {
                    DbField exists = getFieldByCode(fixFieldList, fField);
                    if (exists == null) {
                        DbField field = table.firstOrDefault(f -> f.getTableId().equals(table.getId()) && f.getCode().equals(fField));
                        if (field == null) {
                            throw new Exception("固定字段:" + fField + " 解析失败");
                        }
                        fixFieldList.add(field);
                    }
                }
            }
        } else {
            fixFieldList.addAll(table.getFieldList());
        }
        //判断字段的访问权限
        if (queryOption.isEnableColAccessControl()) {
            for (DataAccessColumnRightService service : columnRightServiceList) {
                fixFieldList = service.checkAccessRight(fixFieldList);
            }
        }
        table.setFieldList(fixFieldList);
        return table;
    }

    private String generateJoinCondition(String tableCode, List<QueryJoinCondition> queryJoinTables) {
        String filter = "";
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(tableCode).append(" ");
        for (QueryJoinCondition queryJoinCondition : queryJoinTables) {
            sbSql.append("LEFT JOIN " + queryJoinCondition.getTableAlias() + " ON " + queryJoinCondition.getCondition() + " ");
        }
        String result = sbSql.toString() + (StringUtils.isEmpty(filter) ? "" : (" WHERE " + filter));
        return result;
    }

    private DbField getFieldByCode(List<DbField> fixFieldList, String code) {
        for (DbField field : fixFieldList) {
            if (field.getCode().equals(code)) {
                return field;
            }
        }
        return null;
    }

    private String getSqlObj(String databaseType, String sqlObj) {
        return lookupFieldParseService.SqlObj(databaseType, sqlObj);
    }

    private String getLookupDisplayField(String databaseType, DbField field, LookupMode colLookupMode, boolean isMainTable, String colLookupDisplay) {
        String tableAlias = null;
        if (isMainTable) {
            tableAlias = field.getCode();
        } else {
            tableAlias = field.getTableCode() + CommonConst.REF_FIELD_SEPARATOR + field.getCode();
        }
        //LookupMode colLo
        String str = getSqlObj(databaseType, tableAlias) + "." + getSqlObj(databaseType, colLookupMode.getDisplayCol()) + " AS " + getSqlObj(databaseType, colLookupDisplay);
        if (!StringUtils.isEmpty(colLookupMode.getExtendCol())) {
            //扩展字段只支持一列
            //扩展字段值为空则会导致整个查询显示为空，部门直接在db中进行字符串拼接。如果需要使用isnull 函数，不同类型数据库有差异，所以单独查询扩展列然后在web端进行拼接
            //str = String.format(" %s+'('+%s+')' ", str, getSqlObj(tableAlias) + "." + getSqlObj(field.getLookupMode().getExtendCol()));
            str = String.format("%s,%s AS %s", str, getSqlObj(databaseType, tableAlias) + "." + getSqlObj(databaseType, colLookupMode.getExtendCol()), getSqlObj(databaseType, colLookupDisplay + "2"));
        }
        return str;
    }
}
