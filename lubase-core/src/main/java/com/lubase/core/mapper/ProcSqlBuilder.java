package com.lubase.core.mapper;

public class ProcSqlBuilder {
    public String getDataList(String procName, String... p1) {
        String parameters = "";
        for (String p : p1) {
            parameters += String.format("'%s',", p);
        }
        parameters = parameters.substring(0, parameters.length() - 1);
        return String.format("exec %s %s", procName, parameters);
    }
}
