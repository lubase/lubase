package com.lubase.orm.model;

import org.springframework.util.StringUtils;

/**
 * 数据库类型
 */
public enum EDatabaseType {
    Sqlserver("sqlserver"),

    Mysql("mysql"),

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
        }
        return null;
    }
}
