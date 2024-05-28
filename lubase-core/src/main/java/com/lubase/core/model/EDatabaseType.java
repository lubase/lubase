package com.lubase.core.model;

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
}
