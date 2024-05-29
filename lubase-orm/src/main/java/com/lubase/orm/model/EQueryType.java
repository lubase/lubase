package com.lubase.orm.model;

public enum EQueryType {
    /**
     * 基本查询
     */
    Basic(1),
    /**
     * datasource查询
     */
    InvokeDataSource(2),
    /**
     * ionvoke Method查询
     */
    InvokeMethod(3);
    private int type;

    EQueryType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
