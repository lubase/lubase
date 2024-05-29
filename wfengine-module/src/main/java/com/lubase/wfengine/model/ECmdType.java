package com.lubase.wfengine.model;

/**
 * 流程按钮类型
 */
public enum ECmdType {
    /**
     * 流程提交
     */
    Submit("1"),
    /**
     * 审核通过
     */
    Approved("2"),

    /**
     * 审批拒绝
     */
    Reject("3"),
    /**
     * 退回
     */
    Return("4"),
    /**
     * 流程转办
     */
    Transfer("5");

    private String type;

    ECmdType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
