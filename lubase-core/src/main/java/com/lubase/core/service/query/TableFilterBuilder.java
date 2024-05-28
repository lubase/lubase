package com.lubase.core.service.query;

import com.lubase.core.TableFilter;
import com.lubase.core.model.ETableFilter2SqlMode;
import com.lubase.core.model.LookupMode;
import com.lubase.core.model.QueryJoinCondition;
import com.lubase.core.model.QueryParamEntity;
import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;
import com.lubase.core.operate.OperateFactory;
import com.lubase.core.util.ServerMacroService;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author A
 */
@Component
public class TableFilterBuilder {

    @Autowired
    private OperateFactory operateFactory;

    @Autowired
    LookupFieldParseService lookupFieldParseService;

    @Autowired
    ServerMacroService serverMacroService;

    /**
     * 把TableFilter 转换为Sql语句
     *
     * @param tableFilter
     * @param tableInfo
     * @param tableAlias
     * @param queryParam
     * @param refFields
     * @return
     */
    public String parseTableFilterToStr(TableFilter tableFilter, DbTable tableInfo, String tableAlias, QueryParamEntity queryParam, Map<String, LookupMode> refFields, List<QueryJoinCondition> queryJoinTables) {
        if (null == tableFilter) {
            return "";
        }
        String result = "";
        if (tableFilter.getChildFilters() != null) {
            for (TableFilter child : tableFilter.getChildFilters()) {
                child.setMainTable(tableFilter.getMainTable());
                String filterString = parseTableFilterToStr(child, tableInfo, tableAlias, queryParam, refFields, queryJoinTables);
                if (StringUtils.isEmpty(filterString)) {
                    continue;
                }
                if (!StringUtils.isEmpty(result)) {
                    result += tableFilter.isAnd() ? " AND " : " OR ";
                }
                result += filterString;
            }
            if (!StringUtils.isEmpty(result)) {
                result = String.format("%s(%s)", tableFilter.isNot() ? "NOT " : "", result);
            }
        } else {
            return parseChildTableFilterToStr(tableFilter, tableInfo, tableAlias, queryParam, refFields, queryJoinTables);
        }
        return result;
    }

    @SneakyThrows
    private String parseChildTableFilterToStr(TableFilter tableFilter, DbTable tableInfo, String tableAlias, QueryParamEntity queryParam, Map<String, LookupMode> refFields, List<QueryJoinCondition> queryJoinTables) {
        if (StringUtils.isEmpty(tableFilter.getFilterValue())
                && tableFilter.getOperateMode() != EOperateMode.IsNull
                && tableFilter.getOperateMode() != EOperateMode.IsNotNull) {
            return "";
        }
        //对检索条件进行宏变量替换
        if (!StringUtils.isEmpty(tableFilter.getFilterValue())) {
            tableFilter.setFilterValue(serverMacroService.getServerMacroByKey(tableFilter.getFilterValue().toString()));
        }

        BaseOperateMode tool = operateFactory.getParseToolByOperateMode(tableFilter.getOperateMode());
        if (tableFilter.getFilterName().indexOf(".") == -1) {
            DbField colInfo = tableInfo.firstOrDefault((field) -> field.getCode().equalsIgnoreCase(tableFilter.getFilterName()));
            if (colInfo == null) {
                String errorMsg = String.format("解析过滤条件失败,过滤字段:%s在表:%s 中不存在", tableFilter.getFilterName(), tableInfo.getCode());
                throw new Exception(errorMsg);
            }
            return tool.ParseMainTableCondition(tableFilter, colInfo, tableAlias, queryParam);
        } else {
            //Map<String, String> joinTables = new HashMap<>();
            List<QueryJoinCondition> joinTables = new ArrayList<>();
            //解析关联查询
            DbField parseCol = lookupFieldParseService.parseLookupField(tableFilter.getFilterName(), tableInfo, refFields, joinTables);
            if (parseCol == null) {
                throw new Exception(String.format("表:%s 过滤条件解析失败:%s", tableInfo.getCode(), tableFilter.getFilterName()));
            }
            String currentColCode = tableFilter.getFilterName().split("\\.")[0];
            DbField currentCol = tableInfo.firstOrDefault(field -> field.getCode().equalsIgnoreCase(currentColCode));
            ETableFilter2SqlMode tableFilter2SqlMode;
            if (isContainsAllJoinTables(queryJoinTables, joinTables)) {
                tableFilter2SqlMode = ETableFilter2SqlMode.Compare;
            } else if (currentCol.getTableFilter2SqlMode() == 1) {
                updateQueryJoinTables(queryJoinTables, joinTables);
                tableFilter2SqlMode = ETableFilter2SqlMode.Compare;
            } else if (currentCol.getTableFilter2SqlMode() == 2) {
                tableFilter2SqlMode = ETableFilter2SqlMode.Exists;
            } else {
                //todo：应该从默认地方取值
                tableFilter2SqlMode = ETableFilter2SqlMode.Exists;
            }
            return tool.parseLookupTableCondition(tableFilter, parseCol, tableFilter2SqlMode, joinTables, queryParam);
        }
    }

    void updateQueryJoinTables(List<QueryJoinCondition> queryJoinTables, List<QueryJoinCondition> joinTables) {
        for (QueryJoinCondition joinCondition : joinTables) {
            // 关联条件都是从lookup字段进行解析，db和reffield是一样的，所以只需判断key即可
            if (queryJoinTables.stream().filter(q -> q.getTableAlias().equals(joinCondition.getTableAlias())).count() == 0) {
                queryJoinTables.add(joinCondition);
            }
        }
    }

    /**
     * 判断query查询的关联表是否包含了filter中所有的lookup关联到表
     *
     * @param queryJoinTables
     * @param joinTables
     * @return
     */
    private boolean isContainsAllJoinTables(List<QueryJoinCondition> queryJoinTables, List<QueryJoinCondition> joinTables) {
        boolean contains = true;
        for (QueryJoinCondition joinCondition : joinTables) {
            // 关联条件都是从lookup字段进行解析，db和reffield是一样的，所以只需判断key即可
            if (queryJoinTables.stream().filter(q -> q.getTableAlias().equals(joinCondition.getTableAlias())).count() == 0) {
                contains = false;
                break;
            }
        }
        return contains;
    }
}
