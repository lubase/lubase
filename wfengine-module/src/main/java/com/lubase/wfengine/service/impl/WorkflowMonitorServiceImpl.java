package com.lubase.wfengine.service.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.auto.entity.*;
import com.lubase.wfengine.dao.*;
import com.lubase.wfengine.model.*;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.remote.RemoteBisDataService;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import com.lubase.wfengine.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WorkflowMonitorServiceImpl implements WorkflowMonitorService {
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
    WFUserInfoService userInfoService;
    @Autowired
    SendEventToRemoteService sendEventToRemoteService;

    @SneakyThrows
    @Override
    public Integer giveUp(LoginUser user, String finsId, String memo) {
        if (StringUtils.isEmpty(finsId)) {
            throw new WarnCommonException("必填参数不能为空");
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(finsId);
        if (!EApprovalStatus.InApproval.getStatus().equals(TypeConverterUtils.object2String(fIns.get("approval_status")))) {
            throw new WarnCommonException("只有处于审批中的流程才能废弃");
        }
        WfServiceEntity serviceEntity = wfServiceDao.getWfServiceById(Long.parseLong(fIns.getService_id()));
        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            wfFInsDao.updateFInsStatus(fIns, EApprovalStatus.GiveUp);
            operatorService.updateOInsWhenFInsGiveUp(user, fIns, memo);

            bisDataUpdateService.sendGiveUpEvent(fIns, serviceEntity.getGive_up_update());
            transactionManager.commit(transactionStatus);
        } catch (Exception ex) {
            transactionManager.rollback(transactionStatus);
            log.error("wf give up error:", ex);
            throw new WarnCommonException("执行失败，请联系管理员");
        }

        return 1;
    }

    @SneakyThrows
    @Override
    public List<TransferInfoModel> getCurrentApprovalUser(String fInsId) {
        if (StringUtils.isEmpty(fInsId)) {
            return new ArrayList<>();
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(fInsId);
        if (EApprovalStatus.GiveUp.getStatus().equals(TypeConverterUtils.object2String(fIns.get("approval_status")))) {
            throw new WarnCommonException("只有处于审批中的流程才能转办");
        }
        String currentTInsId = fIns.getc_tins_id();
        if (StringUtils.isEmpty(currentTInsId)) {
            return new ArrayList<>();
        }
        DbCollection collection = operatorDao.getOInsCollectionByTInsId(currentTInsId);
        List<TransferInfoModel> list = new ArrayList<>();
        for (DbEntity oIns : collection.getData()) {
            if (TypeConverterUtils.object2Integer(oIns.get(WfOInsEntity.COL_PROCESS_STATUS), 0).equals(0)) {
                TransferInfoModel model = new TransferInfoModel();
                model.setOInsId(oIns.getId().toString());
                model.setDisplayName(String.format("任务节点：%s，处理人：%s", oIns.get("task_name"), oIns.get("user_name")));
                list.add(model);
            }
        }
        return list;
    }

    @SneakyThrows
    @Override
    public Integer transfer(String oInsId, String operatorUserId, String toUserId) {
        if (StringUtils.isEmpty(oInsId) || StringUtils.isEmpty(toUserId)) {
            throw new WarnCommonException("必填参数不能为空");
        }
        WfOInsEntity oIns = operatorDao.getOInstanceById(oInsId);
        if (oIns == null) {
            throw new WarnCommonException("流程实例数据不存在！");
        }
        if (oIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败，oIns任务已经被处理");
        }
        if (oIns.getUser_id().equals(toUserId)) {
            throw new WarnCommonException("处理失败，转办人员和当前任务处理人不能一样");
        }
        WfTInsEntity tIns = wfTaskDao.getTinsById(oIns.getTins_id());
        if (tIns == null) {
            throw new WarnCommonException("数据异常：未找到任务实例信息");
        }
        if (tIns.getProcess_status().equals(EProcessStatus.Processed.getStatus())) {
            throw new WarnCommonException("处理失败：任务已经被处理");
        }
        WfTaskEntity taskEntity = wfTaskDao.getTaskEntityById(tIns.getTask_id());
        if (taskEntity.getTask_type().equals(ETaskType.StartEvent.getType())
                || taskEntity.getTask_type().equals(ETaskType.EndEvent.getType())) {
            throw new WarnCommonException("处理失败：开始节点的任务不能转变");
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(tIns.getFins_id());
        OperatorUserModel userModel = null;
        userModel = userInfoService.getOperatorUserByUserIdOrCode(toUserId);
        OperatorUserModel operatorUser = userInfoService.getOperatorUserByUserIdOrCode(operatorUserId);
        if (userModel == null || operatorUser == null) {
            throw new WarnCommonException("转办失败，未查询到转办人员信息或操作人员信息");
        }
        List<OperatorUserModel> userModelList = new ArrayList<>();
        userModelList.add(userModel);
        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            //1、更新处理人实例
            String memo = String.format("操作人员：%s", operatorUser.getUserName());
            operatorService.transferOIns(fIns, oIns.getId(), memo);
            operatorService.createOIns(userModelList, fIns, tIns);
            wfFInsDao.updateFInsProcessUser(fIns, userModelList);
            transactionManager.commit(transactionStatus);

            try {
                OpenEventModel oInsCompleteEventModel = new OpenEventModel(EOpenEventType.CompleteOIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                oInsCompleteEventModel.setUserId(oIns.getUser_id());
                oInsCompleteEventModel.setUserName(oIns.getUser_name());
                oInsCompleteEventModel.setOInsId(oIns.getId().toString());
                //发送处理人完成事件
                sendEventToRemoteService.sendWorkFlowOpenEvent(oInsCompleteEventModel);
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
    public List<ReturnInfoModel> getReturnBackTaskList(String fInsId) {
        if (StringUtils.isEmpty(fInsId)) {
            throw new WarnCommonException("必填参数不能为空");
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(fInsId);
        if (!EApprovalStatus.InApproval.getStatus().equals(TypeConverterUtils.object2String(fIns.get("approval_status")))) {
            throw new WarnCommonException("只有处于审批中的流程才能退回");
        }
        String cTaskId = fIns.getc_task_id();
        List<DbEntity> tInsList = fetTInsListByFInsId(fInsId);
        List<ReturnInfoModel> list = new ArrayList<>();
        for (DbEntity tIns : tInsList) {
            ReturnInfoModel infoModel = new ReturnInfoModel();
            String taskId = tIns.get("task_id").toString();
            if (cTaskId.equals(taskId) || list.stream().anyMatch(t -> t.getTaskId().equals(taskId))) {
                continue;
            }
            infoModel.setTaskId(taskId);
            infoModel.setTInsId(tIns.getId().toString());
            infoModel.setDisplayName(tIns.get("task_name").toString());
            list.add(infoModel);
        }
        return list;
    }

    List<DbEntity> fetTInsListByFInsId(String fInsId) {
        QueryOption queryOption = new QueryOption(WfTInsEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(WfTInsEntity.COL_FINS_ID, fInsId).eq(WfTInsEntity.COL_PROCESS_STATUS, EProcessStatus.Processed.getStatus());
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setBuildLookupField(false);
        queryOption.setFixField("id,task_id,task_name,process_time");
        return dataAccess.queryAllData(queryOption).getData();
    }

    @SneakyThrows
    @Override
    public Integer returnBack(String fInsId, String taskId, String memo, String operatorUserId) {
        if (StringUtils.isEmpty(fInsId) || StringUtils.isEmpty(taskId)) {
            throw new WarnCommonException("必填参数不能为空");
        }
        WfFInsEntity fIns = wfFInsDao.getFinsById(fInsId);
        if (!EApprovalStatus.InApproval.getStatus().equals(TypeConverterUtils.object2String(fIns.get("approval_status")))) {
            throw new WarnCommonException("只有处于审批中的流程才能退回");
        }
        List<ReturnInfoModel> returnInfoModels = getReturnBackTaskList(fInsId);
        if (returnInfoModels.stream().noneMatch(r -> r.getTaskId().equals(taskId))) {
            throw new WarnCommonException("退回失败，请重新选择退回节点");
        }
        String cTInsId = fIns.getc_tins_id();
        String cTaskId = fIns.getc_task_id();
        if (cTInsId.contains(",") || cTaskId.contains(",")) {
            throw new WarnCommonException("当前存在并行任务节点不支持退回");
        }
        OperatorUserModel operatorUser = userInfoService.getOperatorUserByUserIdOrCode(operatorUserId);
        if (operatorUser == null) {
            throw new WarnCommonException("退回失败，未查询到操作人员信息");
        }
        if (StringUtils.isEmpty(memo)) {
            memo = String.format("操作人员：%s", operatorUser.getUserName());
        } else {
            memo = String.format("操作人员：%s，%s", operatorUser.getUserName(), memo);
        }
        WfTaskEntity nextTask = wfTaskDao.getTaskEntityById(taskId);
        WfTaskEntity preTask = wfTaskDao.getTaskEntityById(cTaskId);
        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            //--1 当前任务节点待处理任务都跳过
            operatorService.updateOInsReturn(fIns, cTInsId, memo);
            //-- 2 当前任务节点处理完成
            wfTaskDao.updateTInsStatus(Long.parseLong(cTInsId));
            //-- 3 为选中的节点创建任务事件
            WfTInsEntity nextTaskTIns = wfTaskDao.createTaskIns(fIns, nextTask, preTask);
            BaseNodeService nodeService = taskService.getTaskNodeInfo(nextTask);
            nodeService.createEventJob(fIns, nextTaskTIns);

            transactionManager.commit(transactionStatus);
            try {
                //1广播事件
                OpenEventModel taskCompleteEvent = new OpenEventModel(EOpenEventType.CompleteTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                taskCompleteEvent.setTaskId(cTaskId);
                taskCompleteEvent.setTaskInsId(cTInsId);
                sendEventToRemoteService.sendWorkFlowOpenEvent(taskCompleteEvent);
                //2 广播事件
                OpenEventModel taskCreateEventModel = new OpenEventModel(EOpenEventType.CreateTIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
                taskCreateEventModel.setTaskId(nextTaskTIns.getTask_id());
                taskCreateEventModel.setTaskInsId(nextTaskTIns.getId().toString());
                sendEventToRemoteService.sendWorkFlowOpenEvent(taskCreateEventModel);
            } catch (Exception ex) {
                log.error("广播事件失败returnBack：", ex);
            }
        } catch (Exception ex) {
            log.error("wf returnBack error:", ex);
            transactionManager.rollback(transactionStatus);
            throw new WarnCommonException("处理失败，请联系管理员");
        }
        return 1;
    }
}
