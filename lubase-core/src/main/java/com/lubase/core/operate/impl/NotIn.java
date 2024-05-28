package com.lubase.core.operate.impl;

import com.lubase.core.TableFilter;
import com.lubase.core.model.ETableFilter2SqlMode;
import com.lubase.core.model.QueryJoinCondition;
import com.lubase.core.model.QueryParamEntity;
import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;
import com.lubase.model.DbField;

import java.util.List;

public class NotIn extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.NotIn;
    }

    @Override
    protected String getSqlExpression() {
        return "%s not in (%s)";
    }

    @Override
    public String ParseMainTableCondition(TableFilter tableFilter, DbField colInfo, String tableAlias, QueryParamEntity queryParam) {
        String parmsString = "", result = "";
        String tmpValue = tableFilter.getFilterValue().toString();
        String filterName = sqlObj(tableAlias) + "." + sqlObj(tableFilter.getFilterName());
        for (String value : tmpValue.toString().split(",")) {
            //TODO:宏变量替换
            parmsString += queryParam.addParam(value) + ",";
        }
        if (parmsString.length() > 0) {
            parmsString = parmsString.substring(0, parmsString.length() - 1);
        }
        if (tableFilter.isNot()) {
            result = String.format("%s in (%s)", filterName, parmsString);
        } else {
            result = String.format(getSqlExpression(), filterName, parmsString);
        }
        return result;
    }

    @Override
    public String parseLookupTableCondition(TableFilter tableFilter, DbField parseCol, ETableFilter2SqlMode tableFilter2SqlMode, List<QueryJoinCondition> joinTables, QueryParamEntity queryParam) {
        return super.parseLookupTableCondition(tableFilter, parseCol, tableFilter2SqlMode, joinTables, queryParam);
        //TODO:待实现
    }
}
