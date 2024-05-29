package com.lubase.wfengine.service.impl;

import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfOInsEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.dao.WfFInsDao;
import com.lubase.wfengine.dao.WfOperatorDao;
import com.lubase.wfengine.model.*;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import com.lubase.wfengine.service.WorkOperatorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkOperatorServiceImpl implements WorkOperatorService {

    @Autowired
    DataAccess dataAccess;
    @Autowired
    WfOperatorDao operatorDao;

    @Autowired
    WfFInsDao wfFInsDao;
    @Autowired
    @Qualifier("userColumnServiceImpl")
    IColumnRemoteService columnRemoteService;
    @Autowired
    SendEventToRemoteService sendEventToRemoteService;

    @SneakyThrows
    @Override
    public OperatorUserModel getApplyUserModel(WfFInsEntity fIns) {
        String userId;
        if (fIns.containsKey("apply_user") && fIns.get("apply_user") != null) {
            userId = fIns.get("apply_user").toString();
        } else {
            userId = fIns.getCreate_by().toString();
        }
        OperatorUserModel userModel = new OperatorUserModel();
        userModel.setUserId(userId);
        DbEntity userEntity = columnRemoteService.getCacheDataByKey(userId);
        if (userEntity == null) {
            throw new WarnCommonException(String.format("未找到用户%s，请检查流程设置和业务数据", userId));
        }
        userModel.setUserName(userEntity.get(columnRemoteService.displayCol()).toString());
        return userModel;
    }

    @Override
    public Integer createOIns(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns) {
        DbCollection result = createOIns(userIdList, fIns, tIns, EProcessStatus.UnProcess, null);
        //广播事件
        for (OperatorUserModel user : userIdList) {
            OpenEventModel oInsCreateEventModel = new OpenEventModel(EOpenEventType.CreateOIns, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
            oInsCreateEventModel.setUserId(user.getUserId());
            oInsCreateEventModel.setUserName(user.getUserName());
            oInsCreateEventModel.setOInsId(getOInsId(user.getUserId(), result));
            sendEventToRemoteService.sendWorkFlowOpenEvent(oInsCreateEventModel);
        }
        return 1;
    }

    String getOInsId(String userId, DbCollection collection) {
        for (DbEntity entity : collection.getData()) {
            if (entity.get("user_id").toString().equals(userId)) {
                return entity.getId().toString();
            }
        }
        return "";
    }

    @Override
    public Integer createUnEnabledOIns(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns) {
        createOIns(userIdList, fIns, tIns, EProcessStatus.UnEnabled, null);
        //无需广播事件
        return 1;
    }

    @Override
    public Integer enabledNextOInsForEveryoneInOrder(String tInsId) {
        DbCollection collection = operatorDao.getOInsCollectionByTInsId(tInsId);
        for (DbEntity entity : collection.getData()) {
            if (entity.get(WfOInsEntity.COL_PROCESS_STATUS).toString().equals(EProcessStatus.UnEnabled.getStatus())) {
                entity.put(WfOInsEntity.COL_START_TIME, LocalDateTime.now());
                entity.put(WfOInsEntity.COL_PROCESS_STATUS, EProcessStatus.UnProcess.getStatus());
                break;
            }
        }
        return dataAccess.update(collection);
        //广播事件
    }

    @Override
    public Integer createOInsForStartNode(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns) {
        //无需广播事件
        createOIns(userIdList, fIns, tIns, EProcessStatus.Processed, ETaskType.StartEvent);
        return 1;
    }

    @Override
    public Integer createOInsForEndNode(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns) {
        //无需广播事件
        createOIns(userIdList, fIns, tIns, EProcessStatus.Processed, ETaskType.EndEvent);
        return 1;
    }

    @Override
    public Integer updateOInsForStartNode(Long id) {
        WfOInsEntity oIns = operatorDao.getOInstanceById(id.toString());
        oIns.setProcess_status(EProcessStatus.Processed.getStatus());
        oIns.setProcess_time(LocalDateTime.now());
        oIns.setProcess_memo("提交");
        oIns.setProcess_cmd_des("提交");
        return operatorDao.updateOInsStatus(oIns);
    }

    DbCollection createOIns(List<OperatorUserModel> userIdList, WfFInsEntity fIns, WfTInsEntity tIns, EProcessStatus processStatus, ETaskType taskType) {
        DbCollection collection = dataAccess.getEmptyData(WfOInsEntity.TABLE_CODE);
        //如果当前接节点是
        for (OperatorUserModel user : userIdList) {
            WfOInsEntity entity = new WfOInsEntity();
            entity.setService_id(fIns.getService_id());
            entity.setFlow_id(fIns.getFlow_id());
            entity.setFins_id(fIns.getId().toString());
            entity.setTask_id(tIns.getTask_id());
            entity.setTask_name(tIns.getTask_name());
            entity.setTins_id(tIns.getId().toString());
            entity.setUser_id(user.getUserId());
            entity.setUser_name(user.getUserName());
            if (processStatus.equals(EProcessStatus.UnProcess)) {
                entity.setStart_time(LocalDateTime.now());
            } else if (processStatus.equals(EProcessStatus.Processed)) {
                entity.setStart_time(LocalDateTime.now());
                entity.setProcess_time(LocalDateTime.now());
            }
            if (ETaskType.StartEvent.equals(taskType)) {
                entity.setProcess_cmd_des("提交");
                entity.setProcess_memo("提交");
            } else if (ETaskType.EndEvent.equals(taskType)) {
                entity.setProcess_cmd_des("——");
                entity.setProcess_memo("——");
            }
            entity.setProcess_status(processStatus.getStatus());
            entity.setState(EDBEntityState.Added);
            collection.getData().add(entity);
        }
        dataAccess.update(collection);
        return collection;
    }


    @Override
    public Integer updateOInsStatus(Long id, WFCmdRunModel runModel) {
        WfOInsEntity oIns = operatorDao.getOInstanceById(id.toString());
        oIns.setProcess_status(EProcessStatus.Processed.getStatus());
        oIns.setProcess_time(LocalDateTime.now());
        oIns.setProcess_cmd_id(runModel.getCmdId());
        oIns.setProcess_memo(runModel.getCmdMemo());
        oIns.setProcess_cmd_des(runModel.getCmdIdDesc());
        return operatorDao.updateOInsStatus(oIns);
    }

    @Override
    public Integer tempSaveOInsMemo(Long id, String cmdMemo) {
        WfOInsEntity oIns = operatorDao.getOInstanceById(id.toString());
        oIns.setProcess_memo(cmdMemo);
        return operatorDao.updateOInsStatus(oIns);
    }

    @Override
    public Integer updateOtherOInsSkip(WfFInsEntity fIns, String tinsId, String currentOInsId, String memo) {
        DbCollection collection = operatorDao.getOInsCollectionByTInsId(tinsId);
        for (DbEntity oIns : collection.getData()) {
            if (!oIns.getId().toString().equals(currentOInsId) &&
                    TypeConverterUtils.object2Integer(oIns.get(WfOInsEntity.COL_PROCESS_STATUS), 0).equals(0)) {
                oIns.put(WfOInsEntity.COL_PROCESS_TIME, LocalDateTime.now());
                oIns.put(WfOInsEntity.COL_PROCESS_MEMO, memo);
                oIns.put(WfOInsEntity.COL_PROCESS_STATUS, EProcessStatus.Skipped.getStatus());

                sendOInsCancelEvent(fIns, oIns);
            }
        }
        return dataAccess.update(collection);
    }

    @Override
    public Integer updateOInsReturn(WfFInsEntity fIns, String tinsId, String memo) {
        DbCollection collection = operatorDao.getOInsCollectionByTInsId(tinsId);
        for (DbEntity oIns : collection.getData()) {
            if (TypeConverterUtils.object2Integer(oIns.get(WfOInsEntity.COL_PROCESS_STATUS), 0).equals(0)) {
                oIns.put(WfOInsEntity.COL_PROCESS_TIME, LocalDateTime.now());
                oIns.put(WfOInsEntity.COL_PROCESS_MEMO, memo);
                oIns.put(WfOInsEntity.COL_PROCESS_CMD_DES, "此任务被管理员退回");
                oIns.put(WfOInsEntity.COL_PROCESS_STATUS, EProcessStatus.Processed.getStatus());
                //广播事件
                sendOInsCancelEvent(fIns, oIns);
            }
        }
        return dataAccess.update(collection);
    }

    @Override
    public Integer updateOInsWhenFInsGiveUp(LoginUser user, WfFInsEntity fIns, String memo) {
        String tinsId = fIns.getc_tins_id().toString();
        DbCollection collection = operatorDao.getOInsCollectionByTInsId(tinsId);
        for (DbEntity oIns : collection.getData()) {
            if (TypeConverterUtils.object2Integer(oIns.get(WfOInsEntity.COL_PROCESS_STATUS), 0).equals(0)) {
                oIns.put(WfOInsEntity.COL_PROCESS_TIME, LocalDateTime.now());
                oIns.put(WfOInsEntity.COL_PROCESS_MEMO, "流程废弃任务同步废弃");
                oIns.put(WfOInsEntity.COL_PROCESS_STATUS, EProcessStatus.Skipped.getStatus());
                //广播事件
                sendOInsCancelEvent(fIns, oIns);
            }
        }
        // 增加废弃节点
        WfOInsEntity entity = new WfOInsEntity();
        entity.setService_id(fIns.getService_id());
        entity.setFlow_id(fIns.getFlow_id());
        entity.setFins_id(fIns.getId().toString());
        entity.setTask_id("——");
        entity.setTask_name("——");
        entity.setTins_id("——");
        entity.setUser_id(user.getId().toString());
        entity.setUser_name(String.format("%s(%s)", user.getName(), user.getCode()));
        entity.setStart_time(LocalDateTime.now());
        entity.setProcess_time(LocalDateTime.now());
        entity.setProcess_cmd_des("此流程被管理员废弃");
        entity.setProcess_memo(memo);
        entity.setProcess_status(EProcessStatus.Processed.getStatus());
        entity.setState(EDBEntityState.Added);
        collection.getData().add(entity);
        return dataAccess.update(collection);
    }

    @Override
    public Integer updateOInsWhenTInsTimeout(WfFInsEntity fIns, WfTInsEntity tIns, WFCmdRunModel runModel) {
        String tinsId = tIns.getId().toString();
        DbCollection collection = operatorDao.getOInsCollectionByTInsId(tinsId);
        for (DbEntity oIns : collection.getData()) {
            if (TypeConverterUtils.object2Integer(oIns.get(WfOInsEntity.COL_PROCESS_STATUS), 0).equals(0)) {
                oIns.put(WfOInsEntity.COL_PROCESS_TIME, LocalDateTime.now());
                oIns.put(WfOInsEntity.COL_PROCESS_CMD_ID, runModel.getCmdId());
                oIns.put(WfOInsEntity.COL_PROCESS_MEMO, "任务超时系统自动处理");
                oIns.put(WfOInsEntity.COL_PROCESS_CMD_DES, runModel.getCmdIdDesc());
                oIns.put(WfOInsEntity.COL_PROCESS_STATUS, EProcessStatus.Processed.getStatus());
                //广播事件
                sendOInsEvent(fIns, oIns, EOpenEventType.CompleteOIns);
            }
        }
        return dataAccess.update(collection);
    }

    @Override
    public Integer transferOIns(WfFInsEntity fIns, Long oInsId, String memo) {
        WfOInsEntity oIns = operatorDao.getOInstanceById(oInsId.toString());
        oIns.setProcess_status(EProcessStatus.Processed.getStatus());
        oIns.setProcess_time(LocalDateTime.now());
        oIns.setProcess_memo(memo);
        oIns.setProcess_cmd_des("此任务被管理员转办");
        operatorDao.updateOInsStatus(oIns);
        return 1;
    }

    void sendOInsCancelEvent(WfFInsEntity fIns, DbEntity oIns) {
        sendOInsEvent(fIns, oIns, EOpenEventType.CancelOIns);
    }

    void sendOInsEvent(WfFInsEntity fIns, DbEntity oIns, EOpenEventType eventType) {
        OpenEventModel oInsCreateEventModel = new OpenEventModel(eventType, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
        oInsCreateEventModel.setUserId(oIns.get("user_id").toString());
        oInsCreateEventModel.setUserName(oIns.get("user_name").toString());
        oInsCreateEventModel.setOInsId(oIns.getId().toString());
        sendEventToRemoteService.sendWorkFlowOpenEvent(oInsCreateEventModel);
    }
}
