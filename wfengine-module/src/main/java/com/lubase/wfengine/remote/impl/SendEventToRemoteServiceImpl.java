package com.lubase.wfengine.remote.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.wfengine.auto.entity.WfCallbackEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.config.EngineConfig;
import com.lubase.wfengine.model.OpenEventModel;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class SendEventToRemoteServiceImpl implements SendEventToRemoteService {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendWorkFlowEngineEvent(String serviceId, String dataId, WfTEventEntity entity) {
        if (entity == null) {
            return;
        }
        String msg = JSON.toJSONString(entity);
        try {
            String msgPath = String.format("%s:%s", EngineConfig.MQ_WF_ENGINE_TOPIC, serviceId);
            Map<String, Object> headers = new HashMap<>();
            headers.put("KEYS", dataId);
            rocketMQTemplate.convertAndSend(msgPath, msg, headers);
        } catch (Exception exception) {
            log.error("流程引擎：流程事件发送MQ发生错误：" + msg, exception);
        }
    }

    @Override
    public void sendUpdateBisTableEvent(String serviceId, String dataId, WfCallbackEntity entity) {
        if (entity == null) {
            return;
        }
        String msg = JSON.toJSONString(entity);
        try {
            String msgPath = String.format("%s:%s", EngineConfig.MQ_UPDATE_TABLE_TOPIC, serviceId);
            Map<String, Object> headers = new HashMap<>();
            headers.put("KEYS", dataId);
            rocketMQTemplate.convertAndSend(msgPath, msg, headers);
        } catch (Exception exception) {
            log.error("流程引擎：更新业务表事件发送MQ发生错误：" + msg, exception);
        }
    }

    @Override
    public void sendWorkFlowOpenEvent(OpenEventModel model) {
        if (model == null) {
            return;
        }
        String serviceId = model.getServiceId();
        String dataId = model.getDataId();
        String msg = JSON.toJSONString(model);
        try {
            String msgPath = String.format("%s:%s", EngineConfig.MQ_WF_PUBLIC_TOPIC, serviceId);
            Map<String, Object> headers = new HashMap<>();
            headers.put("KEYS", dataId);
            rocketMQTemplate.convertAndSend(msgPath, msg, headers);
        } catch (Exception exception) {
            log.error("流程引擎：公开事件发送MQ发生错误：" + msg, exception);
        }
    }
}
