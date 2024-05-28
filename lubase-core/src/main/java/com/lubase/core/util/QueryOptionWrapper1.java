package com.lubase.core.util;

import com.lubase.core.QueryOption;

public class QueryOptionWrapper1 {
    private QueryOption queryOption = null;

    QueryOptionWrapper1(QueryOption queryOption) {
        this.queryOption = queryOption;
    }

    public QueryOptionWrapper2 from(String tableCode) {
        queryOption.setTableName(tableCode);
        return new QueryOptionWrapper2(queryOption);
    }
}
