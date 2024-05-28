package com.lubase.wfengine.model;

/**
 * 流程节点类型
 */
public enum ETaskType {

    /**
     * 1 开始
     */
    StartEvent("1"),
    /**
     * 2 结束
     */
    EndEvent("2"),
    /**
     * 3 任意用户处理
     */
    UserTaskForAnyOne("3"),
    /**
     * 4 会签，无顺序
     */
    UserTaskForEveryone("4"),
    /**
     * 5 会签按顺序
     */
    UserTaskForEveryoneInOrder("5"),
    /**
     * 10 并行网关
     */
    ParallelGateWay("10");

    private String type;

    ETaskType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
