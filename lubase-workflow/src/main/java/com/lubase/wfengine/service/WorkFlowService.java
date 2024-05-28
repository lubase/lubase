package com.lubase.wfengine.service;

import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.model.WFCmdRunModel;

public interface WorkFlowService {

    /**
     * 启动流程
     *
     * @param serviceId
     * @param dataId
     * @param userId
     * @return
     */
    Integer startWf(String serviceId, String dataId, String userId);

    /**
     * 退回到开始节点后，再次提交
     *
     * @param oInsId
     * @param userId
     * @return
     */
    Integer restartWf(String oInsId, String userId);

    /**
     * 任务处理
     *
     * @param runModel
     * @return
     */
    Integer run(String userId, WFCmdRunModel runModel);

    /**
     * 暂存审批意见和流程表单数据
     *
     * @param userId
     * @param runModel
     * @return
     */
    Integer saveData(String userId, WFCmdRunModel runModel);

    /**
     * 抽取指定条数的事件，并处理
     *
     * @param
     * @return 流程执行结果，如果不为空则为错误信息
     */
    String processWfTEvent(WfTEventEntity event);

    /**
     * 处理超时任务
     *
     * @return
     */
    void processTimeoutTaskIns();
}
