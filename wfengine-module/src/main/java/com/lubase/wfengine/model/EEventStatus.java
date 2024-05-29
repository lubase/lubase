package com.lubase.wfengine.model;

/**
 * 流程事件处理状态
 */
public enum EEventStatus {
    /**
     * 待处理
     */
    UnProcess("0"),

    /**
     * 处理中
     */
    Processing("1"),
    /**
     * 处理通过
     */
    Processed("2"),
    /**
     * 处理遇到错误
     */
    Error("3");

    private String status;

    EEventStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
