package com.lubase.wfengine.model;

/**
 * 更新业务表字段的场景
 */
public enum EUpdateType {
    /**
     * 1 流程启动
     */
    Start("1"),
    /**
     * 2 流程结束
     */
    End("2"),
    /**
     * 3 流程拒绝
     */
    Reject("3"),
    /**
     * 4 流程废弃
     */
    GiveUp("4"),
    /**
     * 5 命令
     */
    Command("5"),
    /**
     * 6 流转
     */
    Link("6");
    private String type;

    EUpdateType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
