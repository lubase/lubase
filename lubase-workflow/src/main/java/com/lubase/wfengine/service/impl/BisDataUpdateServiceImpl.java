package com.lubase.wfengine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfCallbackEntity;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.model.*;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import com.lubase.wfengine.service.BisDataUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Slf4j
@Service
public class BisDataUpdateServiceImpl implements BisDataUpdateService {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    DataAccess dataAccess;

    @Autowired
    SendEventToRemoteService sendEventToRemoteService;

    @Override
    public WfCallbackEntity sendStartEvent(WfFInsEntity fIns) {
        HashMap<String, Object> updateContentMap = new HashMap<>();
        updateContentMap.put("signed", EApprovalStatus.InApproval.getStatus());
        return createCallbackData(fIns, EUpdateType.Start, JSON.toJSONString(updateContentMap), "");
    }

    @Override
    public void sendEndEvent(WfFInsEntity fIns) {
        HashMap<String, Object> updateContentMap = new HashMap<>();
        updateContentMap.put("signed", EApprovalStatus.Approved.getStatus());
        createCallbackData(fIns, EUpdateType.End, JSON.toJSONString(updateContentMap), "");

        OpenEventModel endEventModel = new OpenEventModel(EOpenEventType.End, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
        endEventModel.setApprovalStatus(EApprovalStatus.Approved);
        sendEventToRemoteService.sendWorkFlowOpenEvent(endEventModel);
    }

    @Override
    public void sendRejectEvent(WfFInsEntity fIns, String reject_update_content) {
        HashMap<String, Object> updateContentMap = new HashMap<>();
        updateContentMap.put("signed", EApprovalStatus.Reject.getStatus());
        addUpdateField(updateContentMap, reject_update_content);
        createCallbackData(fIns, EUpdateType.Reject, JSON.toJSONString(updateContentMap), "");

        OpenEventModel endEventModel = new OpenEventModel(EOpenEventType.End, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
        endEventModel.setApprovalStatus(EApprovalStatus.Reject);
        sendEventToRemoteService.sendWorkFlowOpenEvent(endEventModel);

    }

    @Override
    public void sendGiveUpEvent(WfFInsEntity fIns, String give_up_update_content) {
        HashMap<String, Object> updateContentMap = new HashMap<>();
        updateContentMap.put("signed", EApprovalStatus.GiveUp.getStatus());
        addUpdateField(updateContentMap, give_up_update_content);
        createCallbackData(fIns, EUpdateType.GiveUp, JSON.toJSONString(updateContentMap), "");

        //广播事件
        OpenEventModel endEventModel = new OpenEventModel(EOpenEventType.End, fIns.getService_id(), fIns.getData_id(), fIns.getId().toString());
        endEventModel.setApprovalStatus(EApprovalStatus.GiveUp);
        sendEventToRemoteService.sendWorkFlowOpenEvent(endEventModel);
    }

    @Override
    public void sendCommandEvent(WfFInsEntity fIns, String updateContent, String updateMemo) {
        if (!StringUtils.isEmpty(updateContent)) {
            createCallbackData(fIns, EUpdateType.Command, updateContent, updateMemo);
        }
    }

    @Override
    public void sendLinkEvent(WfFInsEntity fIns, String updateContent, String updateMemo) {
        if (!StringUtils.isEmpty(updateContent)) {
            createCallbackData(fIns, EUpdateType.Link, updateContent, updateMemo);
        }
    }

    HashMap<String, Object> addUpdateField(HashMap<String, Object> mapUpdateContent, String updateContentStr) {
        try {
            if (!StringUtils.isEmpty(updateContentStr)) {
                JSONObject obj = JSON.parseObject(updateContentStr);
                for (String key : obj.keySet()) {
                    mapUpdateContent.put(key, obj.get(key));
                }
            }
        } catch (Exception ex) {
            log.error("执行流程更新业务表失败，请检查配置：" + updateContentStr, ex);
        }
        return mapUpdateContent;
    }

    WfCallbackEntity createCallbackData(WfFInsEntity fIns, EUpdateType updateType, String updateContent, String updateMemo) {
        DbCollection collection = dataAccess.getEmptyData(WfCallbackEntity.TABLE_CODE);
        WfCallbackEntity entity = new WfCallbackEntity();
        entity.setFins_id(fIns.getId().toString());
        entity.setData_id(fIns.getData_id());
        entity.setUpdate_type(updateType.getType());
        entity.setUpdate_content(updateContent);
        entity.setUpdate_memo(updateMemo);
        entity.setStatus(EProcessStatus.UnProcess.getStatus());
        entity.setService_id(fIns.getService_id());
        entity.setState(EDBEntityState.Added);
        collection.getData().add(entity);
        if (dataAccess.update(collection) == 0) {
            log.warn("createCallbackData 方法插入记录数为0，请关注");
        } else {
            sendEventToRemoteService.sendUpdateBisTableEvent(fIns.getService_id(), fIns.getData_id(), entity);
        }
        return entity;
    }
}
