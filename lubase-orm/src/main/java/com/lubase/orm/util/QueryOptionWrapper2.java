package com.lubase.orm.util;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;

public class QueryOptionWrapper2 {
    private QueryOption queryOption = null;

    QueryOptionWrapper2(QueryOption queryOption) {
        this.queryOption = queryOption;
    }

    public QueryOptionWrapper2 where(TableFilter filter) {
        queryOption.setTableFilter(filter);
        return this;
    }

    public QueryOption build() {
        return this.queryOption;
    }
}
