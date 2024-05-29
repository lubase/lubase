package com.lubase.wfengine.model;

/**
 * 处理人实例、任务节点实例处理状态
 */
public enum EProcessStatus {
    /**
     * -1 任务待启动
     */
    UnEnabled("-1"),
    /**
     * 0 待处理
     */
    UnProcess("0"),

    /**
     * 1 已处理
     */
    Processed("1"),

    /**
     * 2 系统跳过
     */
    Skipped("2");

    private String status;

    EProcessStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
