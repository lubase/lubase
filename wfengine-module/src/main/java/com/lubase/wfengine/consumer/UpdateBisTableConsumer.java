package com.lubase.wfengine.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.ServerMacroService;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.core.service.FormRuleService;
import com.lubase.wfengine.auto.entity.WfCallbackEntity;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.config.EngineConfig;
import com.lubase.wfengine.dao.WfServiceDao;
import com.lubase.wfengine.model.EEventStatus;
import com.lubase.wfengine.model.EUpdateType;
import com.lubase.wfengine.service.WorkFlowMacroService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RocketMQMessageListener(topic = "${lubase.wf-engine.update-table-topic:LUBASE_WF_UPDATE_TABLE_TOPIC}", consumerGroup = "${lubase.wf-engine.update-table-consumer-group:LUBASE_WF_UPDATE_TABLE}")
@Service
@Slf4j
public class UpdateBisTableConsumer implements RocketMQListener<WfCallbackEntity> {
    @Autowired
    WfServiceDao serviceDao;
    @Autowired
    DataAccess dataAccess;
    @Autowired
    ServerMacroService serverMacroService;
    @Autowired
    WorkFlowMacroService workFlowMacroService;
    @Autowired
    FormRuleService formRuleService;

    private static final String _flow_ins_id = "flow_ins_id";

    @Override
    public void onMessage(WfCallbackEntity entity) {
        try {
            String errorMsg = processCallbackEvent(entity);
            if (!StringUtils.isEmpty(errorMsg)) {
                writeErrorTip(entity, errorMsg);
            }
        } catch (Exception exception) {
            log.error("process callback update event 执行失败，event id is" + entity.getId(), exception);
        }
    }

    String processCallbackEvent(WfCallbackEntity entity) {
        String errorMsg = "";
        try {
            String serviceId = entity.getService_id();
            String updateContent = entity.getUpdate_content();
            String dataId = entity.getData_id();
            Boolean isStartEvent = entity.getUpdate_type().equals(EUpdateType.Start.getType());
            WfServiceEntity serviceEntity = serviceDao.getWfServiceById(TypeConverterUtils.object2Long(serviceId));
            String mainTable = serviceEntity.getMain_table();
            if (StringUtils.isEmpty(mainTable)) {
                throw new InvokeCommonException(String.format("service %s 没有配置主表 ", serviceEntity.getId()));
            }
            //判断更新是否涉及到子表
            String childTable = "";
            String fkColumn = "";
            boolean isChildTable = isChildTable(updateContent);
            if (isChildTable) {
                childTable = getChildTable(updateContent);
                String mainTableId = dataAccess.initTableInfoByTableCode(mainTable).getId();
                String childTableId = dataAccess.initTableInfoByTableCode(childTable).getId();
                //查询主从表对应关系
                DbEntity tableRelation = formRuleService.getTableRelation(mainTableId, childTableId);
                if (tableRelation == null) {
                    throw new InvokeCommonException("主从表关系设置不正确，请检查");
                }
                fkColumn = tableRelation.get("fk_column_code").toString();
                isChildTable = true;
            }
            QueryOption queryOption = null;
            if (!isChildTable) {
                queryOption = new QueryOption(mainTable);
                queryOption.setTableFilter(new TableFilter("id", dataId));
            } else {
                queryOption = new QueryOption(childTable);
                queryOption.setTableFilter(new TableFilter(fkColumn, dataId));
            }
            queryOption.setFixField("*");

            DbCollection collection = dataAccess.queryAllData(queryOption);
            if (collection.getData().size() > 0) {
                DbEntity workFlowMacroEntity = null;
                //更新配置数据
                HashMap<String, Object> mapUpdateField = getUpdateField(entity.getUpdate_content(), isChildTable);
                for (DbEntity bisData : collection.getData()) {
                    for (String col : mapUpdateField.keySet()) {
                        //回写业务表支持变量，服务端宏变量
                        if (mapUpdateField.get(col).toString().startsWith(ServerMacroService.serverMacroPre)) {
                            bisData.put(col, serverMacroService.getServerMacroByKey(mapUpdateField.get(col).toString()));
                        } else if (mapUpdateField.get(col).toString().startsWith(WorkFlowMacroService.workFlowMacroPre)) {
                            //根据流程宏变量回写业务表
                            if (workFlowMacroEntity == null) {
                                workFlowMacroEntity = workFlowMacroService.getWorkFlowMacro(entity.getFins_id());
                            }
                            bisData.put(col, workFlowMacroEntity.get(mapUpdateField.get(col).toString()));
                        } else {
                            bisData.put(col, mapUpdateField.get(col));
                        }
                    }
                    //更新流程变量
                    if (isStartEvent && !isChildTable) {
                        if (collection.getTableInfo().getFieldList().stream().anyMatch(f -> f.getCode().equals(_flow_ins_id))) {
                            bisData.put(_flow_ins_id, entity.getFins_id());
                        }
                    }
                    //设置流程回写回写标识
                    bisData.put("__update_handle", "wf_write_back");
                }
                dataAccess.update(collection);
                CompleteCallbackEvent(entity);
            } else {
                errorMsg = String.format("未查询到业务数据，请检查流程启动信息，%s", JSON.toJSONString(entity));
                log.warn(errorMsg);
            }
        } catch (Exception ex) {
            log.error("updateBisTableApprovalStatus error:" + JSON.toJSONString(entity), ex);
            errorMsg = ex.getMessage();
        }
        return errorMsg;
    }

    private void writeErrorTip(WfCallbackEntity eventEntity, String errorMsg) {
        Integer errorCount = TypeConverterUtils.object2Integer(eventEntity.get("error_count"), 0) + 1;
        DbCollection collection = dataAccess.getEmptyData(WfCallbackEntity.TABLE_CODE);
        DbEntity entity = new DbEntity();
        entity.put("id", eventEntity.getId());
        entity.put("error_count", errorCount);
        if (errorMsg != null && errorMsg.length() > 400) {
            errorMsg = errorMsg.substring(0, 400);
        }
        entity.put("error_tip", errorMsg);

        //重试5次执行失败不再执行
        Integer maxErrorCount = 3;
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

    void CompleteCallbackEvent(WfCallbackEntity entity) {
        DbCollection collection = dataAccess.getEmptyData(WfCallbackEntity.TABLE_CODE);
        entity.put(WfCallbackEntity.COL_STATUS, EEventStatus.Processed.getStatus());
        entity.setState(EDBEntityState.Modified);
        collection.getData().add(entity);
        dataAccess.update(collection);
    }

    HashMap<String, Object> getUpdateField(String updateCountStr, boolean isChildTable) {
        HashMap<String, Object> mapUpdateContent = new HashMap<>();
        JSONObject obj = JSON.parseObject(updateCountStr);
        for (String key : obj.keySet()) {
            if (isChildTable) {
                if (key.contains(".")) {
                    mapUpdateContent.put(key.split("\\.")[1], obj.get(key));
                } else {
                    log.warn("更新子表字段不能更新主表字段同时配置：" + updateCountStr);
                }
            } else {
                mapUpdateContent.put(key, obj.get(key));
            }

        }
        return mapUpdateContent;
    }

    private String getChildTable(String updateContent) {
        JSONObject obj = JSON.parseObject(updateContent);
        for (String key : obj.keySet()) {
            if (key.contains(".")) {
                return key.split("\\.")[0];
            }
        }
        return null;
    }

    private Boolean isChildTable(String updateContent) {
        if (StringUtils.isEmpty(updateContent)) {
            return false;
        }
        JSONObject obj = JSON.parseObject(updateContent);
        for (String key : obj.keySet()) {
            if (key.contains(".")) {
                return true;
            }
        }
        return false;
    }
}
