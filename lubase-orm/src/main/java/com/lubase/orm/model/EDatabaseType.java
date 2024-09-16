package com.lubase.orm.model;

import org.springframework.util.StringUtils;

/**
 * 数据库类型
 */
public enum EDatabaseType {
    Sqlserver("sqlserver"),
    Mysql("mysql"),
    Oracle("oracle"),
    Sqlite("sqlite"),
    H2("h2"),

    Postgresql("postgresql");

    private String type;

    EDatabaseType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static EDatabaseType getFromString(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        type = type.toLowerCase();
        if (type.equals("mysql")) {
            return EDatabaseType.Mysql;
        } else if (type.equals("sqlserver")) {
            return EDatabaseType.Sqlserver;
        } else if (type.equals("postgresql")) {
            return EDatabaseType.Postgresql;
        } else if (type.equals("oracle")) {
            return EDatabaseType.Oracle;
        } else if (type.equals("sqlite")) {
            return EDatabaseType.Sqlite;
        } else if (type.equals("h2")) {
            return EDatabaseType.H2;
        }
        return null;
    }
}
