package com.lubase.orm.mapper;

import com.lubase.orm.model.EDatabaseType;

public class ProcSqlBuilder {
    public String getDataList(EDatabaseType databaseType, String procName, String... p1) {
        String parameters = "";
        for (String p : p1) {
            parameters += String.format("'%s',", p);
        }
        parameters = parameters.substring(0, parameters.length() - 1);
        if (databaseType.equals(EDatabaseType.Mysql)) {
            return String.format("call %s(%s)", procName, parameters);
        } else {
            return String.format("exec %s %s", procName, parameters);
        }
    }
}
