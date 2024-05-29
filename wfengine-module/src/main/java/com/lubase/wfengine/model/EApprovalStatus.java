package com.lubase.wfengine.model;

/**
 * 流程实例审批状态
 */
public enum EApprovalStatus {
    /**
     * 待提交
     */
    Draft("0"),

    /**
     * 处理中
     */
    InApproval("1"),
    /**
     * 审批通过
     */
    Approved("2"),
    /**
     * 审批拒绝
     */
    Reject("3"),
    /**
     * 流程废弃
     */
    GiveUp("4");

    private String status;

    EApprovalStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
