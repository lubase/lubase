package com.lubase.wfengine.model;

/**
 * 按钮命令类型
 */
public enum ECmdModel {
    /**
     * 流程提交
     */
    Start("1"),

    /**
     *
     */
    Submit("2"),
    /**
     *
     */
    Reject("3"),
    /**
     * 处理通过
     */
    Return("4");

    private String status;

    ECmdModel(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
