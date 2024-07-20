package com.lubase.wfengine.consumer;

import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.config.EngineConfig;
import com.lubase.wfengine.dao.WfFInsDao;
import com.lubase.wfengine.model.EEventStatus;
import com.lubase.wfengine.service.WorkFlowService;
import com.lubase.wfengine.service.WorkTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RocketMQMessageListener(topic = "${lubase.wf-engine.engine-topic:LUBASE_WF_ENGINE_TOPIC}", consumerGroup = "${lubase.wf-engine.engine-consumer-group:LUBASE_WF_ENGINE}")
@Service
@Slf4j
public class EngineEventProcessConsumer implements RocketMQListener<WfTEventEntity> {

    @Autowired
    WfFInsDao wfFInsDao;
    @Autowired
    DataAccess dataAccess;
    @Autowired
    WorkTaskService taskService;

    @Autowired
    WorkFlowService workFlowService;

    //@Override
    public void onMessage(WfTEventEntity entity) {
        try {
            String taskType = taskService.getTaskNodeType(entity.getTask_id());
            log.info("taskType is " + taskType);
            String errorMsg = workFlowService.processWfTEvent(entity);
            if (!StringUtils.isEmpty(errorMsg)) {
                writeErrorTip(entity, errorMsg);
            }
        } catch (Exception exception) {
            log.error("processWftEvent 执行失败，event id is" + entity.getId(), exception);
        }
    }

    private void writeErrorTip(WfTEventEntity eventEntity, String errorMsg) {
        Integer errorCount = TypeConverterUtils.object2Integer(eventEntity.get("error_count"), 0) + 1;
        DbCollection collection = dataAccess.getEmptyData(WfTEventEntity.TABLE_CODE);
        DbEntity entity = new DbEntity();
        entity.put("id", eventEntity.getId());
        entity.put("error_count", errorCount);
        if (errorMsg != null && errorMsg.length() > 400) {
            errorMsg = errorMsg.substring(0, 400);
        }
        entity.put("error_tip", errorMsg);

        //重试5次执行失败不再执行
        /**
         * wf_tevent 执行最大失败次数
         */
        Integer maxErrorCount = 5;
        if (errorCount >= maxErrorCount) {
            entity.put(WfTEventEntity.COL_STATUS, EEventStatus.Error.getStatus());
        }
        entity.setState(EDBEntityState.Modified);
        List<DbField> fieldList = new ArrayList<>();
        for (DbField field : collection.getTableInfo().getFieldList()) {
            if (field.getCode().equals("error_count") || field.getCode().equals("error_tip")
                    || field.getCode().equals("id")) {
                fieldList.add(field);
            } else if (field.getCode().equals("status") && errorCount > maxErrorCount) {
                fieldList.add(field);
            }
        }
        collection.getTableInfo().setFieldList(fieldList);
        collection.getData().add(entity);
        dataAccess.update(collection);
    }
}
