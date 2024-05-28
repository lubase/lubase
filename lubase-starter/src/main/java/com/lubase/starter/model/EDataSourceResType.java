package com.lubase.starter.model;

/**
 * @author A
 */

public enum EDataSourceResType {
    /**
     * 返回对象
     */
    Object(1),
    /**
     * 返回列表
     */
    List(2),
    /**
     * 返回DBCollectoin对象
     */
    DBCollection(3),
    /**
     * 返回字段列表
     */
    FieldList(4);

    private int type;

    EDataSourceResType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
