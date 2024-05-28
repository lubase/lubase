package com.lubase.wfengine.model;

/**
 * 处理者类型
 */
public enum EOperatorType {
    /**
     * 1 指定处理人
     */
    AssignUser("1"),
    /**
     * 2 表单字段值
     */
    FormField("2"),
    /**
     * 其它节点实际处理人
     */
    OtherNode("3");

    private String type;

    EOperatorType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
