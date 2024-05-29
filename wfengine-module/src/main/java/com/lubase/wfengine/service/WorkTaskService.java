package com.lubase.wfengine.service;

import com.lubase.orm.model.DbCollection;
import com.lubase.model.DbField;
import com.lubase.wfengine.auto.entity.*;
import com.lubase.wfengine.model.NextTaskEntity;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.model.WFCmdRunModel;
import com.lubase.wfengine.model.WfTaskFieldModel;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.node.OperatorService;

import java.util.List;

public interface WorkTaskService {
    /**
     * 获取当前节点的下一个节点
     *
     * @param currentTask
     * @param collBisData
     * @param runModel
     * @return
     */
    List<NextTaskEntity> getNextTasks2(WfTaskEntity currentTask, DbCollection collBisData, WFCmdRunModel runModel);

    /**
     * 根据任务节点获取后去详细信息
     *
     * @param taskEntity
     * @return
     */
    BaseNodeService getTaskNodeInfo(WfTaskEntity taskEntity);

    /**
     * 获取节点表单字段操作列表
     *
     * @param formFieldList 流程设置的表单可操作的字段列表
     * @param taskEntity    流程节点信息
     * @return
     */
    List<DbField> getTaskNodeFormField(List<DbField> formFieldList, WfTaskEntity taskEntity);

    /**
     * 获取节点自定义设置
     *
     * @param taskEntity
     * @return
     */
    List<WfTaskFieldModel> getTaskCustomSetting(WfTaskEntity taskEntity);
    
    List<DbField> getTaskNodeFormField(List<DbField> formFieldList, List<WfTaskFieldModel> nodeCustomSetting);

    /**
     * 根据任务节点id获取节点类型
     *
     * @param id
     * @return
     */
    String getTaskNodeType(String id);

    /**
     * 根据任务节点id获取配置的处理者里列表
     *
     * @param id
     * @return
     */
    List<WfOperEntity> getOperatorListByTaskId(String id);

    /**
     * 获取节点处理人信息
     *
     * @param
     * @return
     */
    List<OperatorUserModel> getNodeProcessUserList(WfFInsEntity fIns, WfTaskEntity taskEntity, DbCollection bisData);

    /**
     * 根据任务节点id获取处理命令
     *
     * @param id
     * @return
     */
    List<WfCmdEntity> getCmdListByTaskId(String id);

    /**
     * 根据处理者类型获取对应的服务实例
     *
     * @param operatorType
     * @return
     */
    OperatorService getOperatorServiceInstance(String operatorType);

    /**
     * 判断节点是否可以继续流转
     *
     * @param taskEntity
     * @return
     */
    boolean isAllUserProcessByTIns(WfTaskEntity taskEntity, WfTInsEntity tInsEntity);
}
