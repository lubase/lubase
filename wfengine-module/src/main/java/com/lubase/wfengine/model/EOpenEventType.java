package com.lubase.wfengine.model;

/**
 * 流程对外事件类型
 */
public enum EOpenEventType {
    /**
     * 流程启动
     */
    Start(1),
    /**
     * 到达节点
     */
    CreateTIns(2),
    /**
     * 为处理人创建任务
     */
    CreateOIns(3),
    /**
     * 处理人完成任务
     */
    CompleteOIns(4),
    /**
     * 节点任务完成
     */
    CompleteTIns(5),
    /**
     * 取消处理人任务
     */
    CancelOIns(6),
    /**
     * 流程结束
     */
    End(9);

    private Integer type;

    EOpenEventType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }
}
