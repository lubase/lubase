package com.lubase.wfengine.service;

import com.lubase.orm.model.LoginUser;
import com.lubase.wfengine.model.ReturnInfoModel;
import com.lubase.wfengine.model.TransferInfoModel;

import java.util.List;

/**
 * 流程监控服务
 */
public interface WorkflowMonitorService {
    /**
     * 管理员废弃流程
     *
     * @param finsId
     * @return
     */
    Integer giveUp(LoginUser userInfo, String finsId, String memo);

    /**
     * 根据流程实例获取当前未办理人
     *
     * @param fInsId
     * @return
     */
    List<TransferInfoModel> getCurrentApprovalUser(String fInsId);

    /**
     * 管理员转办流程
     *
     * @return
     */
    Integer transfer(String oInsId, String operatorUserId, String toUserId);

    /**
     * 获取可以退回的流程节点列表
     *
     * @param fInsId
     * @return
     */
    List<ReturnInfoModel> getReturnBackTaskList(String fInsId);

    /**
     * 管理员退回某个流程
     *
     * @param fInsId
     * @param taskId
     * @param memo
     * @return
     */
    Integer returnBack(String fInsId, String taskId, String memo, String operatorUserId);
}
