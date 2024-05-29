package com.lubase.wfengine.service;

import com.lubase.orm.model.LoginUser;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.model.WFCmdRunModel;

import java.util.List;

public interface WorkOperatorService {


    /**
     * 根据流程实例获取申请人信息
     *
     * @param fIns
     * @return
     */
    OperatorUserModel getApplyUserModel(WfFInsEntity fIns);

    /**
     * 创建处理人实例信息
     *
     * @param userIdList
     * @param fIns
     * @param tIns
     * @return
     */
    Integer createOIns(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns);

    /**
     * 会签按顺序节点，生成未启用的任务
     *
     * @param userIdList
     * @param fIns
     * @param tIns
     * @return
     */
    Integer createUnEnabledOIns(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns);

    /**
     * 首次提交流程为开始节点创建处理人实例
     *
     * @param userIdList
     * @param fIns
     * @param tIns
     * @return
     */
    Integer createOInsForStartNode(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns);

    Integer createOInsForEndNode(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns);

    /**
     * 退回到开始节点后，再次提时更新处理人实例
     *
     * @param id 流程实例id
     * @return
     */
    Integer updateOInsForStartNode(Long id);

    /**
     * 会签按顺序节点，按顺序启动待处理任务
     *
     * @return
     */
    Integer enabledNextOInsForEveryoneInOrder(String tInsId);

    /**
     * 更新任务处理状态
     *
     * @param id
     * @param runModel
     * @return
     */
    Integer updateOInsStatus(Long id, WFCmdRunModel runModel);

    /**
     * 暂存审批意见
     *
     * @param id
     * @param cmdMemo
     * @return
     */
    Integer tempSaveOInsMemo(Long id, String cmdMemo);

    /**
     * 任意处理人处理节点，其他任务设置为跳过
     *
     * @param tinsId
     * @param currentOInsId
     * @return
     */
    Integer updateOtherOInsSkip(WfFInsEntity fIns, String tinsId, String currentOInsId, String memo);

    /**
     * 流程废弃时处理未处理任务
     *
     * @param user
     * @param fIns
     * @param memo
     * @return
     */
    Integer updateOInsWhenFInsGiveUp(LoginUser user, WfFInsEntity fIns, String memo);

    /**
     * 任务节点超时自动处理
     *
     * @param fIns
     * @param tIns
     * @return
     */
    Integer updateOInsWhenTInsTimeout(WfFInsEntity fIns, WfTInsEntity tIns, WFCmdRunModel runModel);

    /**
     * 流程监控退回时，处理当前任务节点
     *
     * @param fIns
     * @param tinsId
     * @param memo
     * @return
     */
    Integer updateOInsReturn(WfFInsEntity fIns, String tinsId, String memo);

    /**
     * 管理员转变任务实例
     *
     * @param fIns
     * @param oInsId
     * @param memo
     * @return
     */
    Integer transferOIns(WfFInsEntity fIns, Long oInsId, String memo);
}
