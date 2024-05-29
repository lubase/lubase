package com.lubase.orm.util;


import com.lubase.orm.QueryOption;

/**
 * queryoption 包装器
 *
 * @author A
 */
public class QueryOptionWrapper {
    private QueryOption queryOption = null;

    QueryOptionWrapper() {
        this.queryOption = new QueryOption();
    }

    public static QueryOptionWrapper1 select(String fixField) {
        QueryOptionWrapper queryOptionWrapper = new QueryOptionWrapper();
        queryOptionWrapper.queryOption.setFixField(fixField);
        return new QueryOptionWrapper1(queryOptionWrapper.queryOption);
    }
}

