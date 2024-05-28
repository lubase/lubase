package com.lubase.core.model;

import java.util.HashMap;

/**
 * sql语句拼接对象，用于执行dataAccess.update。切记不能拼接sql
 *
 * @author A
 */
public class SqlEntity extends HashMap<String, Object> {

    private Integer paramIndex = 0;

    public Integer getParamIndex() {
        return this.paramIndex;
    }

    /**
     * 添加一个参数，返回参数的名字
     *
     * @param paramValue 参数值
     * @return 参数的名字
     */
    public String addParam(Object paramValue) {
        paramIndex++;
        String paramName = String.format("#{p%s}", paramIndex);
        this.put(String.format("p%s", paramIndex), paramValue);
        return paramName;
    }

    private String sqlStr = "";

    public String getSqlStr() {
        return this.sqlStr;
    }

    /**
     * 切记不能拼接sql。出现sql注入后果自负
     *
     * @param sqlStr
     */
    public void setSqlStr(String sqlStr) {
        this.sqlStr += sqlStr;
        this.put("__sql__", this.sqlStr);
    }
}
