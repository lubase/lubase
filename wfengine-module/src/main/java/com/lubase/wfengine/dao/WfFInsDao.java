package com.lubase.wfengine.dao;

import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.ServerMacroService;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.model.EApprovalStatus;
import com.lubase.wfengine.model.OperatorUserModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WfFInsDao {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    ServerMacroService serverMacroService;

    public String getFInsName(WfServiceEntity serviceEntity, DbCollection bisData) {
        DbEntity entity = bisData.getData().get(0);
        String template = TypeConverterUtils.object2String(serviceEntity.get("fins_name_template"), "");
        if (StringUtils.isEmpty(template)) {
            return entity.getId().toString();
        }
        //"{@@S.time}--{address}{@@S.datetime}";
        try {
            String pattern = "\\{@@[^{]*}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(template);
            while (m.find()) {
                String matcherStr = m.group(0);
                String key = m.group(0).replaceAll("[{}]", "");
                System.out.println(key);
                String key2 = serverMacroService.getServerMacroByKey(key);
                template = template.replace(matcherStr, key2);
            }
            for (DbField f : bisData.getTableInfo().getFieldList()) {
                String display = "";
                if (entity.getRefData() != null && entity.getRefData().containsKey(f.getCode() + "NAME")) {
                    display = entity.getRefData().get(f.getCode() + "NAME");
                } else {
                    display = TypeConverterUtils.object2String(entity.get(f.getCode()), "");
                }
                template = template.replace(String.format("{%s}", f.getCode()), display);
            }
        } catch (Exception ex) {
            String msg = String.format("生成流程实例名字错误，模板%s，数据id%s", template, entity.getId());
            log.error(msg, ex);
        }
        return template;
    }

    /**
     * 是否可以启动新流程
     *
     * @param serviceId
     * @param dataId
     * @return
     */
    public boolean canStartWorkFlow(String serviceId, String dataId) {
        QueryOption queryOption = new QueryOption(WfFInsEntity.TABLE_CODE);
        TableFilterWrapper wrapper = TableFilterWrapper.and().eq(WfFInsEntity.COL_SERVICE_ID, serviceId)
                .eq(WfFInsEntity.COL_DATA_ID, dataId);
        queryOption.setTableFilter(wrapper.build());
        queryOption.setFixField(WfFInsEntity.COL_ID);
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection.getData().size() == 0;
    }

    /**
     * 创建工作流实例
     *
     * @param serviceId
     * @param flowId
     * @param dataId
     * @param name
     * @return
     */
    public WfFInsEntity creatFlowInstance(String serviceId, String serviceName, String flowId, String dataId, String name, String userId) {
        DbCollection collection = dataAccess.getEmptyData(WfFInsEntity.TABLE_CODE);
        WfFInsEntity wfFIns = new WfFInsEntity();
        wfFIns.setState(EDBEntityState.Added);
        wfFIns.setService_id(serviceId);
        wfFIns.setService_name(serviceName);
        wfFIns.setFlow_id(flowId);
        wfFIns.setStart_time(LocalDateTime.now());
        wfFIns.setApproval_status(EApprovalStatus.InApproval.getStatus());
        wfFIns.setData_id(dataId);
        wfFIns.setName(name);
        wfFIns.put("apply_user", userId);
        collection.getData().add(wfFIns);
        dataAccess.update(collection);
        return wfFIns;
    }

    @SneakyThrows
    public void updateFInsTaskInfo(WfFInsEntity fIns, WfTInsEntity tIns) {
        DbCollection collection = dataAccess.queryById(WfFInsEntity.TABLE_CODE, fIns.getId());
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            entity.setState(EDBEntityState.Modified);
            entity.put(WfFInsEntity.COL_C_TASK_ID, tIns.getTask_id());
            entity.put(WfFInsEntity.COL_C_TASK_NAME, tIns.getTask_name());
            entity.put(WfFInsEntity.COL_C_TINS_ID, tIns.getId().toString());
            dataAccess.update(collection);
        } else {
            throw new InvokeCommonException(String.format("流程实例:%s不存在", fIns.getId()));
        }
    }

    @SneakyThrows
    public void updateFInsStatus(WfFInsEntity fIns, EApprovalStatus status) {
        DbCollection collection = dataAccess.queryById(WfFInsEntity.TABLE_CODE, fIns.getId());
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            entity.setState(EDBEntityState.Modified);
            entity.put(WfFInsEntity.COL_C_TASK_ID, "");
            entity.put(WfFInsEntity.COL_C_TASK_NAME, "");
            entity.put(WfFInsEntity.COL_C_TINS_ID, "");
            entity.put(WfFInsEntity.COL_PROCESS_USER_ID, "");
            entity.put(WfFInsEntity.COL_APPROVAL_STATUS, status.getStatus());
            entity.put(WfFInsEntity.COL_END_TIME, LocalDateTime.now());
            dataAccess.update(collection);
        } else {
            throw new InvokeCommonException(String.format("流程实例:%s不存在", fIns.getId()));
        }
    }

    @SneakyThrows
    public void updateFInsProcessUser(WfFInsEntity fIns, List<OperatorUserModel> userIdList) {
        DbCollection collection = dataAccess.queryById(WfFInsEntity.TABLE_CODE, fIns.getId());
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            entity.setState(EDBEntityState.Modified);
            String name = userIdList.stream().map(OperatorUserModel::getUserName).collect(Collectors.joining(","));
            entity.put(WfFInsEntity.COL_PROCESS_USER_ID, name);
            dataAccess.update(collection);
        } else {
            throw new InvokeCommonException(String.format("流程实例:%s不存在", fIns.getId()));
        }
    }

    /**
     * 根据id 获取流程实例id
     *
     * @param id
     * @return
     */
    public WfFInsEntity getFinsById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Long longId = Long.parseLong(id);
        List<WfFInsEntity> list = dataAccess.queryById(WfFInsEntity.TABLE_CODE, longId).getGenericData(WfFInsEntity.class);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
