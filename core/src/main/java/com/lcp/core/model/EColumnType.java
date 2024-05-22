package com.lcp.core.model;

/**
 * 字段类型
 */
public enum EColumnType {
    BigInt(0),
    Text(1),
    Date(2),
    Boolean(3),
    Int(4),
    Numeric(5),
    /**
     * 关联代码表
     */
    CodeData(6),
    /**
     * 非代码表的关联
     */
    Lookup(7),
    Document(8),
    Image(9),
    /**
     * 服务列
     */
    RemoteServiceColumn(10);

    private int index;

    EColumnType(int index) {
        this.index = index;
    }

    public Integer getIndex() {
        return this.index;
    }
    public String getStringValue(){
        return String.valueOf(this.index);
    }

    public static EColumnType fromIndex(int index) {
        if (index == 0) {
            return EColumnType.BigInt;
        } else if (index == 1) {
            return EColumnType.Text;
        } else if (index == 2) {
            return EColumnType.Date;
        } else if (index == 3) {
            return EColumnType.Boolean;
        } else if (index == 4) {
            return EColumnType.Int;
        } else if (index == 5) {
            return EColumnType.Numeric;
        } else if (index == 6) {
            return EColumnType.CodeData;
        } else if (index == 7) {
            return EColumnType.Lookup;
        } else if (index == 8) {
            return EColumnType.Document;
        } else if (index == 9) {
            return EColumnType.Image;
        } else if (index == 10) {
            return EColumnType.RemoteServiceColumn;
        } else {
            return EColumnType.Text;
        }
    }
}
