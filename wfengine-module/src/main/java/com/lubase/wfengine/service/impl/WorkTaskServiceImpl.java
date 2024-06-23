package com.lubase.wfengine.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.orm.model.DbCollection;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EAccessGrade;
import com.lubase.wfengine.auto.entity.*;
import com.lubase.wfengine.dao.WfCmdDao;
import com.lubase.wfengine.dao.WfLinkDao;
import com.lubase.wfengine.dao.WfOperatorDao;
import com.lubase.wfengine.dao.WfTaskDao;
import com.lubase.wfengine.model.*;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.node.OperatorService;
import com.lubase.wfengine.service.DynamicPredicateService;
import com.lubase.wfengine.service.WorkTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
@Service
public class WorkTaskServiceImpl implements ExtendAppLoadCompleteService, WorkTaskService {
    @Autowired
    WfLinkDao linkDao;

    @Autowired
    WfTaskDao taskDao;

    @Autowired
    WfOperatorDao operDao;

    @Autowired
    WfCmdDao cmdDao;
    List<BaseNodeService> baseNodeServices;

    @Autowired
    List<OperatorService> operatorServices;

    @Autowired
    DynamicPredicateService dynamicPredicateService;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (baseNodeServices == null) {
            baseNodeServices = new ArrayList<>();
            Map<String, BaseNodeService> serviceMap = applicationContext.getBeansOfType(BaseNodeService.class);
            for (String key : serviceMap.keySet()) {
                baseNodeServices.add(serviceMap.get(key));
            }
            log.info(String.format("BaseNodeService列表总数量为：%s，明细为：%s", baseNodeServices.size(), JSON.toJSONString(baseNodeServices)));
        }

    }

    @Override
    public void clearData() {
        baseNodeServices = null;
    }

    @SneakyThrows
    @Override
    public List<NextTaskEntity> getNextTasks2(WfTaskEntity currentTask, DbCollection collBisData, WFCmdRunModel runModel) {
        String cmdId = null;
        if (runModel != null) {
            cmdId = runModel.getCmdId();
        }
        List<WfLinkEntity> links = linkDao.getNextLinkForTask(currentTask.getFlow_id(), currentTask.getId().toString(), cmdId);
        //暂时只有并行网关节点对多个后续节点进行处理，其他节点需要根据线上的条件进行过滤
        String taskType = currentTask.getTask_type();
        List<String> taskIds = new ArrayList<>();
        Map<String, WfLinkEntity> mapLink = new HashMap<>();
        if (!ETaskType.ParallelGateWay.getType().equals(taskType) && links.size() > 1) {
            links.sort(Comparator.comparingInt(WfLinkEntity::getPriority));
            for (WfLinkEntity linkEntity : links) {
                //待实现根据线上的条件进行过滤
                if (!checkLinkFilter(collBisData, linkEntity)) {
                    continue;
                }
                taskIds.add(linkEntity.getEnd_task_id());
                mapLink.put(linkEntity.getEnd_task_id(), linkEntity);
            }
        } else {
            for (WfLinkEntity linkEntity : links) {
                taskIds.add(linkEntity.getEnd_task_id());
                mapLink.put(linkEntity.getEnd_task_id(), linkEntity);
            }
        }
        if (!ETaskType.ParallelGateWay.getType().equals(taskType) && taskIds.size() > 1) {
            String msg = "";
            for (WfLinkEntity link : links) {
                msg += link.getMemo() + "";
            }
            throw new WarnCommonException(String.format("流程设置错误，在非并行网关节点之后找到多个后续处理节点，流转路径： %s", msg));
        }
        List<WfTaskEntity> taskEntityList = taskDao.getTaskByIds(taskIds.toArray(String[]::new));
        List<NextTaskEntity> nextTaskEntities = new ArrayList<>();
        for (WfTaskEntity task : taskEntityList) {
            NextTaskEntity nextTask = new NextTaskEntity();
            nextTask.cloneFromNewEntity(task);
            nextTask.setFromLink(mapLink.get(task.getId().toString()));
            nextTaskEntities.add(nextTask);
        }
        return nextTaskEntities;
    }

    @SneakyThrows
    Boolean checkLinkFilter(DbCollection collBisData, WfLinkEntity linkEntity) {
        if (StringUtils.isEmpty(linkEntity.getLogic_condition()) || linkEntity.getLogic_condition().equals("{}")) {
            return true;
        }
        TableFilter filter = JSON.parseObject(linkEntity.getLogic_condition(), TableFilter.class);
        Predicate<DbEntity> predicate = dynamicPredicateService.parseTableFilterToPredicate(filter);
        return predicate.test(collBisData.getData().get(0));
    }

    @Override
    public BaseNodeService getTaskNodeInfo(WfTaskEntity taskEntity) {
        BaseNodeService baseNodeService = null;
        for (BaseNodeService service : baseNodeServices) {
            if (service.getTaskType().getType().equals(taskEntity.getTask_type())) {
                baseNodeService = service;
                break;
            }
        }
        return baseNodeService;
    }

    @SneakyThrows
    @Override
    public List<DbField> getTaskNodeFormField(List<DbField> formFieldList, WfTaskEntity taskEntity) {
        List<WfTaskFieldModel> taskFieldModelList = getTaskCustomSetting(taskEntity);
        if (taskFieldModelList == null || taskFieldModelList.size() == 0) {
            return formFieldList;
        }
        Map<String, WfTaskFieldModel> taskFieldList = new HashMap<>();
        for (WfTaskFieldModel field : taskFieldModelList) {
            taskFieldList.put(field.getId(), field);
        }
        return mergeCustomFormToRegisterFieldInfo(formFieldList, taskFieldList);
    }

    List<DbField> mergeCustomFormToRegisterFieldInfo(List<DbField> formFieldList, Map<String, WfTaskFieldModel> mapTaskFieldList) {
        //设置表单资源编辑属性
        if (mapTaskFieldList.isEmpty()) {
            return formFieldList;
        }
        Integer settingTag = 1;
        for (DbField field : formFieldList) {
            if (!mapTaskFieldList.containsKey(field.getId())) {
                continue;
            }
            WfTaskFieldModel mapField = mapTaskFieldList.get(field.getId());
            if (settingTag.equals(mapField.getHidden())) {
                field.setVisible(EAccessGrade.Invisible.getIndex());
            } else if (settingTag.equals(mapField.getReadonly())) {
                field.setVisible(EAccessGrade.Read.getIndex());
            }
            if (settingTag.equals(mapField.getRequired())) {
                field.setIsNull(0);
            }
            //表单字段可编辑字段默认均不再处理
            if (field.getFieldAccess().getIndex() > EAccessGrade.Read.getIndex()) {
                field.setColDefault("");
            }
        }
        return formFieldList;
    }

    @SneakyThrows
    @Override
    public List<WfTaskFieldModel> getTaskCustomSetting(WfTaskEntity taskEntity) {
        List<WfTaskFieldModel> taskFieldModelList = null;
        String taskSetting = taskEntity.getSetting();
        if (StringUtils.isEmpty(taskSetting)) {
            return null;
        }
        WFTaskSettingModel settingModel = null;
        try {
            settingModel = JSON.parseObject(taskSetting, WFTaskSettingModel.class);
        } catch (Exception exception) {
            log.error(String.format("任务节点%s转换taskSettingModel失败", taskEntity.getId()), exception);
        }
        if (settingModel == null) {
            throw new WarnCommonException(String.format("任务节点%s转换taskSettingModel失败", taskEntity.getId()));
        }
        return settingModel.getFieldListAccessRight();
    }

    @Override
    public List<DbField> getTaskNodeFormField(List<DbField> formFieldList, List<WfTaskFieldModel> taskFieldModelList) {
        if (taskFieldModelList == null || taskFieldModelList.size() == 0) {
            return formFieldList;
        }
        Map<String, WfTaskFieldModel> taskFieldList = new HashMap<>();
        for (WfTaskFieldModel field : taskFieldModelList) {
            taskFieldList.put(field.getId(), field);
        }
        return mergeCustomFormToRegisterFieldInfo(formFieldList, taskFieldList);
    }

    //TODO：此方法需要用缓存
    @Override
    public String getTaskNodeType(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        WfTaskEntity taskEntity = taskDao.getTaskEntityById(id);
        if (taskEntity != null) {
            return taskEntity.getTask_type();
        } else {
            return null;
        }
    }

    @Override
    public List<WfOperEntity> getOperatorListByTaskId(String id) {
        return operDao.getOperatorByTaskId(id);
    }

    @Override
    public List<OperatorUserModel> getNodeProcessUserList(WfFInsEntity fIns, WfTaskEntity taskEntity, DbCollection bisData) {
        List<OperatorUserModel> userIdList = new ArrayList<>();
        if (taskEntity.getRebuild_operator() == null || !taskEntity.getRebuild_operator()) {
            userIdList = operDao.getNodeValidProcessUserList(fIns.getId().toString(), taskEntity.getId().toString());
        }
        if (userIdList.size() == 0) {
            //生成处理人
            List<WfOperEntity> operatorList = getOperatorListByTaskId(taskEntity.getId().toString());
            for (WfOperEntity operator : operatorList) {
                OperatorService operatorService = getOperatorServiceInstance(operator.getOper_type());
                userIdList.addAll(operatorService.getUserIdList(taskEntity, operator, fIns, bisData));
            }
        }
        List<OperatorUserModel> noRepeatList = new ArrayList<>();
        for (OperatorUserModel userModel : userIdList) {
            if (noRepeatList.stream().anyMatch(s -> s.getUserId().equals(userModel.getUserId()))) {
                continue;
            }
            noRepeatList.add(userModel);
        }
        return noRepeatList;
    }

    @Override
    public List<WfCmdEntity> getCmdListByTaskId(String id) {
        return cmdDao.getCmdListByTaskId(id);
    }

    @Override
    public OperatorService getOperatorServiceInstance(String operatorType) {
        OperatorService operatorService = null;
        for (OperatorService service : operatorServices) {
            if (service.getOperatorType().getType().equals(operatorType)) {
                operatorService = service;
                break;
            }
        }
        return operatorService;
    }

    @Override
    public boolean isAllUserProcessByTIns(WfTaskEntity taskEntity, WfTInsEntity tInsEntity) {
        List<WfOInsEntity> oInsEntities = operDao.getOInstanceByTInsId(tInsEntity.getId().toString());
        return oInsEntities.stream().noneMatch(d -> d.getProcess_status().equals("0") || d.getProcess_status().equals("-1"));
    }
}
