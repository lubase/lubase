package com.lubase.wfengine.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.wfengine.auto.entity.*;
import com.lubase.wfengine.dao.*;
import com.lubase.wfengine.model.*;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.remote.RemoteBisDataService;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import com.lubase.wfengine.service.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkFlowServiceImpl implements WorkFlowService {
    @Autowired
    WfServiceDao wfServiceDao;
    @Autowired
    WfFInsDao wfFInsDao;
    @Autowired
    DataAccess dataAccess;
    @Autowired
    WfOperatorDao operatorDao;
    @Autowired
    WfTaskDao wfTaskDao;
    @Autowired
    WfTEventService eventService;
    @Autowired
    WorkTaskService taskService;

    @Autowired
    WorkOperatorService operatorService;
    @Autowired
    WfCmdDao cmdDao;
    @Autowired
    BisDataUpdateService bisDataUpdateService;

    @Autowired
    RemoteBisDataService remoteBisDataService;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    ChangeDataSourceService changeDataSourceService;
    @Autowired
    CustomFormDataService customFormDataService;

    @Autowired
    WFUserInfoService userInfoService;
    @Autowired
    SendEventToRemoteService sendEventToRemoteService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Autowired
    AppHolderService appHolderService;
    /**
     * 评分数据存储的key
     */
    private String ratingDataStr = "_rating_data";

    @SneakyThrows
    @Override
    public Integer startWf(@NonNull String serviceId, @NonNull String dataId, @NonNull String userId) {
        WfServiceEntity serviceEntity = wfServiceDao.getWfServiceById(TypeConverterUtils.object2Long(serviceId));
        if (serviceEntity == null || StringUtils.isEmpty(serviceEntity.getCurrent_flow_id())) {
            throw new WarnCommonException(String.format("未找到业务场景或当前流程设置不正确，id：%s", serviceId));
        }
        if (!wfFInsDao.canStartWorkFlow(serviceId, dataId)) {
            throw new WarnCommonException("流程已经启动，无需启动");
        }
        WfTaskEntity startNode = wfTaskDao.getStartTask(serviceEntity.getCurrent_flow_id());
        if (startNode == null) {
            throw new WarnCommonException("流程配置错误，没有找到开始节点");
        }
        log.info("启动中。。。。dataId:{}", dataId);
        execStartEvent(dataId, serviceEntity, startNode, userId);
        log.info("启动完成,dataId:{}", dataId);
        return 1;
    }

    @SneakyThrows
    private void execStartEvent(String dataId, WfServiceEntity serviceEntity, WfTaskEntity taskEntity, String userId) {
        log.info("启动流程获取业务数据,dataId:{}", dataId);
        DbCollection bisData = remoteBisDataService.getBisData(serviceEntity.getId().toString(), dataId);
        OperatorUserModel userModel = userInfoService.getOperatorUserByUserIdOrCode(userId);
        if (userModel == null) {
            throw new WarnCommonException("申请人不存在" + userId);
        } else {
            userId = userModel.getUserId();
        }
        if (bisData.getData().size() == 0) {
            throw new WarnCommonException("未找到业务数据");
        }
        String finsName = wfFInsDao.getFInsName(serviceEntity, bisData);

        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            //1 创建流程实例，任务实例
            WfFInsEntity fIns = wfFInsDao.creatFlowInstance(serviceEntity.getId().toString(), serviceEntity.getName(), serviceEntity.getCurrent_flow_id(), dataId, finsName, userId);
            WfTInsEntity tIns = wfTaskDao.createTaskInsWithProcessed(fIns, taskEntity);
            //2 更新流程实例当前节点信息
            wfFInsDao.updateFInsTaskInfo(fIns, tIns);
            //3 增加申请记录，方便审批历史展示
            List<OperatorUserModel> userIdList = new ArrayList<>();
            userIdList.add(operatorService.getApplyUserModel(fIns));
            operatorService.createOInsForStartNode(userIdList, fIns, tIns);
            //4 发送流程开始事件
            WfCallbackEntity callbackEntity = bisDataUpdateService.sendStartEvent(fIns);

            //6 生成下一个任务节点任务
            List<NextTaskEntity> nextTasks = taskService.getNextTasks2(taskEntity, bisData, null);
            if (nextTasks.size() == 0) {
                throw new WarnCommonException("没有找到下一个处理节点");
            } else if (nextTasks.size() != 1) {
                String msg = "";
                for (NextTaskEntity nextTaskEntity : nextTasks) {
                    msg += nextTaskEntity.getTask_name() + ",";
                }
                throw new WarnCommonException("没有找到唯一的下一个处理节点");
            }
            List<WfTInsEntity> nextTInsList = createNextTaskJob(fIns, nextTasks, taskEntity);
            //广播业务更新回到事件。放到这里是因为此动作需要和事务保持原子性
            if (callbackEntity != null) {
                sendEventToRemoteService.sendUpdateBisTableEvent(fIns.getService_id(), fIns.getData_id(), callbackEntity);
            }
            transactionManager.commit(transactionStatus);
            try {
                //广播流程开始事件
                OpenEventModel startEventModel = new OpenEventModel(EOpenEventType.Start, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                sendEventToRemoteService.sendWorkFlowOpenEvent(startEventModel);
                //广播下个任务节点创建事件
                createNextTaskOpenEvent(fIns, nextTInsList);
            } catch (Exception ex) {
                log.error("广播事件失败：", ex);
            }
        } catch (Exception ex) {
            log.error("wf execStartEvent error:", ex);
            if (!transactionStatus.isCompleted()) {
                transactionManager.rollback(transactionStatus);
            }
            throw ex;
        }
    }

    private List<WfTInsEntity> createNextTaskJob(WfFInsEntity fIns, List<NextTaskEntity> nextTasks, WfTaskEntity preTask) {
        //创建下一个节点
        List<WfTInsEntity> tInsEntityList = new ArrayList<>();
        for (WfTaskEntity nextTask : nextTasks) {
            Boolean parallelCompleted = true;
            //TODO:判断聚会的逻辑在这里
            // if(nextTask.getType()=="JuHe")

            if (parallelCompleted) {
                //1 创建下一个节点的实例，并且生成event任务
                WfTInsEntity nextTaskTIns = wfTaskDao.createTaskIns(fIns, nextTask, preTask);
                BaseNodeService nodeService = taskService.getTaskNodeInfo(nextTask);
                nodeService.createEventJob(fIns, nextTaskTIns);

                nextTaskTIns.put("_eng_flag", nextTask.getTask_type().equals(ETaskType.EndEvent.getType()) ? 1 : 0);
                tInsEntityList.add(nextTaskTIns);
            }
        }
        //执行link上配置的更新条件
        for (NextTaskEntity nextTaskEntity : nextTasks) {
            String memo = nextTaskEntity.getFromLink().getId() + TypeConverterUtils.object2String(nextTaskEntity.getFromLink().getMemo(), "");
            bisDataUpdateService.sendLinkEvent(fIns, nextTaskEntity.getFromLink().getUpdate_content(), memo);
        }
        return tInsEntityList;
    }

    private void createNextTaskOpenEvent(WfFInsEntity fIns, List<WfTInsEntity> nextTInsList) {
        for (WfTInsEntity tIns : nextTInsList) {
            //结束节点不广播节点创建事件
            if (TypeConverterUtils.object2Integer(tIns.get("_eng_flag"), 0).equals(1)) {
                continue;
            }
            //1 广播事件
            OpenEventModel taskCreateEventModel = new OpenEventModel(EOpenEventType.CreateTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
            taskCreateEventModel.setTaskId(tIns.getTask_id());
            taskCreateEventModel.setTaskInsId(tIns.getId().toString());
            sendEventToRemoteService.sendWorkFlowOpenEvent(taskCreateEventModel);
        }
    }

    @SneakyThrows
    @Override
    public Integer run(String userId, WFCmdRunModel runModel) {
        if (StringUtils.isEmpty(runModel.getOInsId()) || StringUtils.isEmpty(runModel.getCmdId())) {
            throw new WarnCommonException("必填参数不能为空");
        }
        WfOInsEntity oIns = operatorDao.getOInstanceById(runModel.getOInsId());
        if (oIns == null || !oIns.getUser_id().equals(userId)) {
            throw new WarnCommonException("您没有权限处理此条任务！");
        }
        if (oIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败，oIns任务已经被处理");
        }
        WfCmdEntity cmdEntity = cmdDao.getWfCmdById(runModel.getCmdId());
        if (cmdEntity == null || !cmdEntity.getTask_id().equals(oIns.getTask_id())) {
            throw new WarnCommonException("处理失败：操作命令信息不匹配");
        }
        Integer requireProcessMemo = TypeConverterUtils.object2Integer(cmdEntity.get("require_process_memo"), 0);
        if (requireProcessMemo.equals(1) && StringUtils.isEmpty(runModel.getCmdMemo())) {
            throw new WarnCommonException("请填写处理意见");
        }
        WfTInsEntity tIns = wfTaskDao.getTinsById(oIns.getTins_id());
        if (tIns == null) {
            throw new WarnCommonException("数据异常：未找到任务实例信息");
        }
        if (tIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败：tIns任务已经被处理");
        }
        WfTaskEntity taskEntity = wfTaskDao.getTaskEntityById(tIns.getTask_id());
        if (taskEntity.getTask_type().equals(ETaskType.StartEvent.getType())
                || taskEntity.getTask_type().equals(ETaskType.EndEvent.getType())) {
            throw new WarnCommonException("节点类型错误，请联系管理员");
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(tIns.getFins_id());
        WfServiceEntity serviceEntity = wfServiceDao.getWfServiceById(Long.parseLong(fIns.getService_id()));
        DbCollection bisData = remoteBisDataService.getBisData(fIns.getService_id(), fIns.getData_id());
        OperatorUserModel userModel = null;
        if (!StringUtils.isEmpty(runModel.getDesignationUser())) {
            userModel = userInfoService.getOperatorUserByUserIdOrCode(runModel.getDesignationUser());
        }

        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            //1、更新处理人实例
            runModel.setCmdIdDesc(cmdEntity.getCmd_des());
            operatorService.updateOInsStatus(oIns.getId(), runModel);
            OpenEventModel oInsCompleteEventModel = new OpenEventModel(EOpenEventType.CompleteOIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
            oInsCompleteEventModel.setUserId(oIns.getUser_id());
            oInsCompleteEventModel.setUserName(oIns.getUser_name());
            oInsCompleteEventModel.setOInsId(oIns.getId().toString());
            List<WfTInsEntity> nextTInsList = new ArrayList<>();
            OpenEventModel taskCompleteEvent = null;
            Boolean sendRejectEvent = false;
            if (cmdEntity.getCmd_type().equals(ECmdType.Reject.getType())) {
                //2、更新任务实例状态（如果需要）
                wfTaskDao.updateTInsStatus(tIns.getId());
                wfFInsDao.updateFInsStatus(fIns, EApprovalStatus.Reject);
                operatorService.updateOtherOInsSkip(fIns, tIns.getId().toString(), oIns.getId().toString(), "流程废弃系统自动跳过");
                bisDataUpdateService.sendCommandEvent(fIns, cmdEntity.getUpdate_content(), cmdEntity.getId() + cmdEntity.getCmd_des());
                //发送节点完成事件
                taskCompleteEvent = new OpenEventModel(EOpenEventType.CompleteTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                taskCompleteEvent.setTaskId(tIns.getTask_id());
                taskCompleteEvent.setTaskInsId(tIns.getId().toString());
                //发送流程驳回结束事件
                sendRejectEvent = true;
            } else if (cmdEntity.getCmd_type().equals(ECmdType.Approved.getType())
                    || cmdEntity.getCmd_type().equals(ECmdType.Return.getType())) {
                boolean continueRun = false;
                if (taskEntity.getTask_type().equals(ETaskType.UserTaskForAnyOne.getType())) {
                    continueRun = true;
                    //更新其他处理实例状态为已自动跳过
                    operatorService.updateOtherOInsSkip(fIns, tIns.getId().toString(), oIns.getId().toString(), "竞争节点系统自动跳过");
                } else if (taskEntity.getTask_type().equals(ETaskType.UserTaskForEveryone.getType())
                        || taskEntity.getTask_type().equals(ETaskType.UserTaskForEveryoneInOrder.getType())) {
                    //如果是退回按钮则一个退回都退回
                    if (cmdEntity.getCmd_type().equals(ECmdType.Return.getType())) {
                        continueRun = true;
                        //更新其他处理实例状态为已自动跳过
                        operatorService.updateOtherOInsSkip(fIns, tIns.getId().toString(), oIns.getId().toString(), "会签节点有人退回此任务自动跳过");
                    } else {
                        continueRun = taskService.isAllUserProcessByTIns(taskEntity, tIns);
                    }
                }

                bisDataUpdateService.sendCommandEvent(fIns, cmdEntity.getUpdate_content(), cmdEntity.getId() + cmdEntity.getCmd_des());
                if (continueRun) {
                    //2、更新任务实例状态（如果需要）
                    wfTaskDao.updateTInsStatus(tIns.getId());
                    //广播事件
                    taskCompleteEvent = new OpenEventModel(EOpenEventType.CompleteTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                    taskCompleteEvent.setTaskId(tIns.getTask_id());
                    taskCompleteEvent.setTaskInsId(tIns.getId().toString());
                    //3、寻找下个节点并流转
                    List<NextTaskEntity> nextTasks = taskService.getNextTasks2(taskEntity, bisData, runModel);
                    if (nextTasks.size() == 0) {
                        throw new WarnCommonException("没有找到下一个处理节点");
                    }
                    nextTInsList = createNextTaskJob(fIns, nextTasks, taskEntity);
                } else if (taskEntity.getTask_type().equals(ETaskType.UserTaskForEveryoneInOrder.getType())) {
                    //4、为下一个处理人启用任务
                    operatorService.enabledNextOInsForEveryoneInOrder(tIns.getId().toString());
                    //TODO：任务开启未发送任务关闭事件
                }
            } else if (cmdEntity.getCmd_type().equals(ECmdType.Transfer.getType())) {
                List<OperatorUserModel> userModelList = new ArrayList<>();
                if (userModel == null) {
                    throw new WarnCommonException("转办失败，未查询到转办人员信息");
                }
                userModelList.add(userModel);
                operatorService.createOIns(userModelList, fIns, tIns);
                wfFInsDao.updateFInsProcessUser(fIns, userModelList);
            } else {
                throw new InvokeCommonException("cmdType未实现:" + cmdEntity.getCmd_type());
            }
            transactionManager.commit(transactionStatus);
            try {
                //发送处理人完成事件
                sendEventToRemoteService.sendWorkFlowOpenEvent(oInsCompleteEventModel);
                //发送任务完成事件
                if (taskCompleteEvent != null) {
                    sendEventToRemoteService.sendWorkFlowOpenEvent(taskCompleteEvent);
                }
                //发送下个节点创建事件
                createNextTaskOpenEvent(fIns, nextTInsList);
                //发送流程驳回结束事件
                if (sendRejectEvent) {
                    bisDataUpdateService.sendRejectEvent(fIns, serviceEntity.getReject_update());
                }
            } catch (Exception ex) {
                log.error("广播run事件失败", ex);
            }
        } catch (Exception ex) {
            log.error("wf run error:", ex);
            if (!transactionStatus.isCompleted()) {
                transactionManager.rollback(transactionStatus);
            }
            throw ex;
        }
        return 1;
    }

    @SneakyThrows
    @Override
    public Integer saveData(String userId, WFCmdRunModel runModel) {
        if (StringUtils.isEmpty(runModel.getOInsId())) {
            throw new WarnCommonException("必填参数不能为空");
        }
        WfOInsEntity oIns = operatorDao.getOInstanceById(runModel.getOInsId());
        if (oIns == null || !oIns.getUser_id().equals(userId)) {
            throw new WarnCommonException("您没有权限处理此条任务！");
        }
        if (oIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败，oIns任务已经被处理");
        }
        WfTInsEntity tIns = wfTaskDao.getTinsById(oIns.getTins_id());
        if (tIns == null) {
            throw new WarnCommonException("数据异常：未找到任务实例信息");
        }
        if (tIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败：tIns任务已经被处理");
        }
        WfTaskEntity taskEntity = wfTaskDao.getTaskEntityById(tIns.getTask_id());
        WfFInsEntity fIns = wfFInsDao.getFinsById(tIns.getFins_id());

        DbCollection collFlow = dataAccess.queryById(WfFLowEntity.TABLE_CODE, Long.parseLong(fIns.getFlow_id()), WfFLowEntity.COL_FORM_ID);
        if (collFlow.getData().size() == 0) {
            throw new WarnCommonException("数据异常：流程图未找到");
        }
        String formId = TypeConverterUtils.object2String(collFlow.getData().get(0).get(WfFLowEntity.COL_FORM_ID), "");
        if (StringUtils.isEmpty(formId)) {
            throw new WarnCommonException("数据异常：流程表单未配置，请联系管理员");
        }
        DmCustomFormEntity formEntity = customFormDataService.selectById(formId);
        if (null == formEntity) {
            throw new WarnCommonException(String.format("表单%s不存在", formId));
        }
        //1、更新处理人实例
        if (!StringUtils.isEmpty(runModel.getCmdMemo())) {
            operatorService.tempSaveOInsMemo(oIns.getId(), runModel.getCmdMemo());
        }

        //2、更新评分表单数据
        if (runModel.getData().containsKey(ratingDataStr) && runModel.getData().get(ratingDataStr) != null) {
            String ratingFormId = TypeConverterUtils.object2String(taskEntity.get("rating_form"), "");
            if (!StringUtils.isEmpty(ratingFormId)) {
                DmCustomFormEntity ratingForm = customFormDataService.selectById(ratingFormId);
                DbEntity ratingEntity = JSON.parseObject(runModel.getData().get(ratingDataStr).toString(), DbEntity.class);
                if (ratingEntity != null) {
                    DbCollection collection = dataAccess.getEmptyData(ratingForm.getTable_code());
                    collection.getData().add(ratingEntity);
                    collection.setClientMode();
                    collection.getTableInfo().setFieldList(customFormDataService.getFormFieldSetting(ratingForm));
                    dataAccess.update(collection);
                }
            }
        }

        DbCollection collection = dataAccess.getEmptyData(formEntity.getTable_code());
        collection.getData().add(runModel.getData());
        collection.setClientMode();
        // 3、更新表单信息
        // 3.1、拼接自定义表单可编辑字段
        if (formEntity.getForm_type().equals("custom")) {
            collection.getTableInfo().setFieldList(customFormDataService.getFormFieldSetting(formEntity));
        } else if (StringUtils.isEmpty(formEntity.getForm_type())) {
            throw new InvokeCommonException("表单类型设置不正确，无法保存");
        }
        //3.2、拼接流程任务节点字段可更新权限设置
        List<DbField> taskFieldList = taskService.getTaskNodeFormField(collection.getTableInfo().getFieldList(), taskEntity);
        collection.getTableInfo().setFieldList(taskFieldList);
        //3.3、调用dataAccess的更新方法，此方法会验证数据权限
        dataAccess.update(collection);

        return 1;
    }

    @Override
    @SneakyThrows
    public Integer restartWf(String oInsId, String userId) {
        WfOInsEntity oIns = operatorDao.getOInstanceById(oInsId);
        if (oIns == null || !oIns.getUser_id().equals(userId)) {
            throw new WarnCommonException("您没有权限处理此条任务！");
        }
        if (oIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败，oIns任务已经被处理");
        }
        WfTInsEntity tIns = wfTaskDao.getTinsById(oIns.getTins_id());
        if (tIns == null) {
            throw new WarnCommonException("数据异常：未找到任务实例信息");
        }
        if (tIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败：tIns任务已经被处理");
        }
        WfTaskEntity taskEntity = wfTaskDao.getTaskEntityById(tIns.getTask_id());
        if (!taskEntity.getTask_type().equals(ETaskType.StartEvent.getType())) {
            throw new WarnCommonException("节点类型错误，请联系管理员");
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(tIns.getFins_id());
        DbCollection bisData = remoteBisDataService.getBisData(fIns.getService_id(), fIns.getData_id());

        OpenEventModel oInsCompleteEventModel = new OpenEventModel(EOpenEventType.CompleteOIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
        oInsCompleteEventModel.setUserId(oIns.getUser_id());
        oInsCompleteEventModel.setUserName(oIns.getUser_name());
        oInsCompleteEventModel.setOInsId(oIns.getId().toString());

        OpenEventModel taskCompleteEvent = new OpenEventModel(EOpenEventType.CompleteTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
        taskCompleteEvent.setTaskId(tIns.getTask_id());
        taskCompleteEvent.setTaskInsId(tIns.getId().toString());

        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            //1、更新处理人实例
            operatorService.updateOInsForStartNode(oIns.getId());
            //2、更新任务实例状态（如果需要）
            wfTaskDao.updateTInsStatus(tIns.getId());
            //3、寻找下个节点并流转
            List<NextTaskEntity> nextTasks = taskService.getNextTasks2(taskEntity, bisData, null);
            if (nextTasks.size() == 0) {
                throw new WarnCommonException("没有找到下一个处理节点");
            }
            List<WfTInsEntity> nextTInsList = createNextTaskJob(fIns, nextTasks, taskEntity);
            transactionManager.commit(transactionStatus);
            try {
                //发送处理人完成事件
                sendEventToRemoteService.sendWorkFlowOpenEvent(oInsCompleteEventModel);
                //发送任务完成事件
                sendEventToRemoteService.sendWorkFlowOpenEvent(taskCompleteEvent);
                //发送下个节点创建事件
                createNextTaskOpenEvent(fIns, nextTInsList);
            } catch (Exception ex) {
                log.error("广播restartWf事件失败", ex);
            }
        } catch (Exception ex) {
            log.error("wf run error:", ex);
            if (!transactionStatus.isCompleted()) {
                transactionManager.rollback(transactionStatus);
            }
            throw ex;
        }
        return 1;
    }

    @Override
    public String processWfTEvent(WfTEventEntity event) {
        WfFInsEntity fIns = wfFInsDao.getFinsById(event.getFins_id());
        if (!fIns.getApproval_status().equals(EApprovalStatus.InApproval.getStatus())) {
            log.warn(String.format("processWfTEvent异常：非审批中流程不能不能处理,event id %s", event.getFlow_id()));
            return "";
        }
        String errorMsg = "";
        WfTaskEntity task = wfTaskDao.getTaskEntityById(event.getTask_id());
        BaseNodeService nodeInfo = taskService.getTaskNodeInfo(task);
        WfTInsEntity tIns = wfTaskDao.getTinsById(event.getTins_id());
        DbCollection bisData = remoteBisDataService.getBisData(fIns.getService_id(), fIns.getData_id());

        try {
            LoginUser loginUser = new LoginUser();
            loginUser.setId(event.getCreate_by());
            appHolderService.setUser(loginUser);
        } catch (Exception exception) {
            log.error("流程引擎初始化用户信息失败", exception);
        }
        // 获取业务数据后需要切换数据库 20230228
        changeDataSourceService.changeDataSourceByTableCode(WfOperEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            // --  以下逻辑需要包含在一个事务，否则会出问题
            //处理具体的事件，并更新事件状态
            boolean completed = nodeInfo.runTask(fIns, task, tIns, event, bisData);
            //判断是否是结束节点，如果是结束，则流程结束，否则更新流程状态
            //为结束节点创建了tevent是不是可以认为都是正常结束的？
            if (task.getTask_type().equals(ETaskType.EndEvent.getType())) {
                wfFInsDao.updateFInsStatus(fIns, EApprovalStatus.Approved);
            } else {
                //更新流程实例当前任务节点信息
                //TODO: 并发网关后会存在多个同时进行的任务节点，此时当前节点状态设置可能会存在问题
                wfFInsDao.updateFInsTaskInfo(fIns, tIns);
            }
            //如果节点的runTask 内容已经完成则标记event状态为完成，否则标记为处理中?
            if (completed) {
                eventService.eventProcess(event.getId());
            } else {
                eventService.eventProcessing(event.getId());
            }
            //如果是结束节点，则发送结束事件给业务操作服务
            if (task.getTask_type().equals(ETaskType.EndEvent.getType())) {
                bisDataUpdateService.sendEndEvent(fIns);
            }
            transactionManager.commit(transactionStatus);
        } catch (Exception exception) {
            log.error("wf processWfTEvent error:", exception);
            transactionManager.rollback(transactionStatus);
            //写入错误日志
            errorMsg = exception.getMessage();
        }
        return errorMsg;
    }

    @Override
    public void processTimeoutTaskIns() {
        QueryOption queryOption = new QueryOption(WfTInsEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(WfTInsEntity.COL_PROCESS_STATUS, EProcessStatus.UnProcess.getStatus());
        filterWrapper.lt(WfTInsEntity.COL_DEAD_LINE, LocalDateTime.now().format(formatter));
        queryOption.setTableFilter(filterWrapper.build());
        List<WfTInsEntity> listTIns = dataAccess.queryAllData(queryOption).getGenericData(WfTInsEntity.class);
        for (WfTInsEntity tIns : listTIns) {
            WfFInsEntity fIns = wfFInsDao.getFinsById(tIns.getFins_id());
            WfTaskEntity taskEntity = wfTaskDao.getTaskEntityById(tIns.getTask_id());
            WfCmdEntity cmdEntity = getAutoRunCmd(taskEntity.getId().toString());
            WFCmdRunModel runModel = new WFCmdRunModel();
            if (cmdEntity == null) {
                log.error("设置了超时时间，但是没有设置自动处理命令。任务节点id：" + taskEntity.getId());
                continue;
            }
            runModel.setCmdId(cmdEntity.getId().toString());
            runModel.setCmdIdDesc(cmdEntity.getCmd_des());
            DbCollection bisData = remoteBisDataService.getBisData(fIns.getService_id(), fIns.getData_id());

            changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
            TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
            TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
            try {
                //1、所有未处理任务取消并标记为自动处理
                operatorService.updateOInsWhenTInsTimeout(fIns, tIns, runModel);
                //2、标记此节点为处理状态
                wfTaskDao.updateTInsStatus(tIns.getId());
                //3、寻找下个节点并流转
                List<NextTaskEntity> nextTasks = taskService.getNextTasks2(taskEntity, bisData, runModel);
                if (nextTasks.size() == 0) {
                    throw new WarnCommonException("没有找到下一个处理节点");
                }
                List<WfTInsEntity> nextTInsList = createNextTaskJob(fIns, nextTasks, taskEntity);
                bisDataUpdateService.sendCommandEvent(fIns, cmdEntity.getUpdate_content(), cmdEntity.getId() + cmdEntity.getCmd_des());
                transactionManager.commit(transactionStatus);

                try {
                    //1、广播任务完成事件
                    OpenEventModel taskCompleteEvent = new OpenEventModel(EOpenEventType.CompleteTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                    taskCompleteEvent.setTaskId(tIns.getTask_id());
                    taskCompleteEvent.setTaskInsId(tIns.getId().toString());
                    sendEventToRemoteService.sendWorkFlowOpenEvent(taskCompleteEvent);
                    //2、发送下个节点创建事件
                    createNextTaskOpenEvent(fIns, nextTInsList);
                } catch (Exception ex) {
                    log.error("广播事件失败processTimeoutTaskIns：", ex);
                }
            } catch (Exception ex) {
                log.error("processTimeoutTaskIns error:", ex);
                transactionManager.rollback(transactionStatus);
            }
        }
    }

    WfCmdEntity getAutoRunCmd(String taskId) {
        List<WfCmdEntity> cmdList = cmdDao.getCmdListByTaskId(taskId);
        for (WfCmdEntity cmdEntity : cmdList) {
            if (cmdEntity.getTimeout_process()) {
                return cmdEntity;
            }
        }
        return null;
    }

}
