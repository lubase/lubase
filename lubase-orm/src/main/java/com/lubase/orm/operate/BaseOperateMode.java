package com.lubase.orm.operate;

import com.lubase.orm.TableFilter;
import com.lubase.orm.model.ETableFilter2SqlMode;
import com.lubase.orm.model.QueryJoinCondition;
import com.lubase.orm.model.QueryParamEntity;
import com.lubase.model.DbField;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author A
 */
public abstract class BaseOperateMode {
    /**
     * 获取逻辑连接符
     *
     * @return
     */
    protected abstract EOperateMode getOperateMode();

    /**
     * SQL 表达式
     *
     * @return
     */
    protected abstract String getSqlExpression();

    public Object getLogicValue(Object filterValue) {
        return filterValue;
    }

    public String ParseMainTableCondition(TableFilter tableFilter, DbField colInfo, String tableAlias, QueryParamEntity queryParam) {
        String paramString = "", result = "";
        Object tmpValue = getLogicValue(tableFilter.getFilterValue());
        String filterName = sqlObj(tableAlias) + "." + sqlObj(tableFilter.getFilterName());

        result = String.format(getSqlExpression(), filterName, queryParam.addParam(tmpValue));
        if (tableFilter.isNot()) {
            result = "NOT " + result;
        }
        return result;
    }

    public String parseLookupTableCondition(TableFilter tableFilter, DbField parseCol, ETableFilter2SqlMode tableFilter2SqlMode, List<QueryJoinCondition> joinTables, QueryParamEntity queryParam) {
        String result = "";
        Object tmpValue = getLogicValue(tableFilter.getFilterValue());
        //TODO: App.GetMacroValue(tableFilter.FilterValue);
        String filterName = sqlObj(parseCol.getTableCode()) + "." + sqlObj(parseCol.getCode());
        //宏变量替换
        String condition = String.format(getSqlExpression(), filterName, queryParam.addParam(tmpValue));
        switch (tableFilter2SqlMode) {
            case Compare:
                result = condition;
                break;
            case Exists:
                result = String.format("EXISTS(SELECT 1 FROM %s AND %s)", getJoinTableSql(joinTables), condition);
                break;
            default:
                break;
        }
        if (tableFilter.isNot()) {
            result = "NOT " + result;
        }
        return result;
    }

    protected String sqlObj(String sqlObj) {
        return sqlObj;
    }

    private String getJoinTableSql(List<QueryJoinCondition> joinTables) {
        String mainTableFilterSql = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (QueryJoinCondition queryJoinCondition : joinTables) {
            //第一条记录为主表
            if (stringBuilder.length() == 0) {
                stringBuilder.append(queryJoinCondition.getTableAlias() + " ");
                mainTableFilterSql = queryJoinCondition.getCondition();
            } else {
                stringBuilder.append(String.format(" left join %s on %s", queryJoinCondition.getTableAlias(), queryJoinCondition.getCondition()));
            }
        }
        if (!StringUtils.isEmpty(mainTableFilterSql)) {
            stringBuilder.append(String.format(" where %s", mainTableFilterSql));
        }
        return stringBuilder.toString();
    }
}
