package com.lubase.wfengine.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.*;
import com.lubase.core.model.CustomFormVO;
import com.lubase.core.model.FormButtonVO;
import com.lubase.core.service.RenderFormService;
import com.lubase.core.util.ClientMacro;
import com.lubase.wfengine.auto.entity.*;
import com.lubase.wfengine.dao.*;
import com.lubase.wfengine.model.*;
import com.lubase.wfengine.service.WFApprovalService;
import com.lubase.wfengine.service.WorkTaskService;
import com.sun.istack.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class WFApprovalServiceImpl implements WFApprovalService {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    WfOperatorDao operatorDao;

    @Autowired
    WfCmdDao cmdDao;

    @Autowired
    WfFInsDao fInsDao;
    @Autowired
    WfTaskDao taskDao;

    @Autowired
    WfServiceDao serviceDao;

    @Autowired
    RenderFormService formDataService;

    @Autowired
    WorkTaskService taskService;

    @Autowired
    AppHolderService appHolderService;

    @SneakyThrows
    @Override
    public CustomPageSetting getRefPageSetting(String oInsId, String userId) {
        WfOInsEntity oIns = operatorDao.getOInstanceById(oInsId);
        if (oIns == null) {
            throw new WarnCommonException("未找到待办处理数据");
        }
        WfTaskEntity currentTaskEntity = taskDao.getTaskEntityById(oIns.getTask_id());
        return getRefPageSetting(currentTaskEntity);
    }

    @SneakyThrows
    @Override
    public WFApprovalFormVO getApprovalForm(String oInsId, String userId) {
        WFApprovalFormVO approvalFormVO = new WFApprovalFormVO();
        WfOInsEntity oIns = operatorDao.getOInstanceById(oInsId);
        WfTaskEntity currentTaskEntity = taskDao.getTaskEntityById(oIns.getTask_id());
        if (oIns == null) {
            throw new WarnCommonException("未找到待办处理数据");
        }
        if (!oIns.getUser_id().equals(userId) || !oIns.getProcess_status().equals(EProcessStatus.UnProcess.getStatus())) {
            approvalFormVO.setReadonly(true);
        } else {
            approvalFormVO.setReadonly(false);
        }
        //非只读时，显示按钮列表和之前保存的处理意见
        List<WFCmdModel> cmdModelList = null;
        if (approvalFormVO.getReadonly()) {
            cmdModelList = new ArrayList<>();
        } else {
            cmdModelList = getCmdList(oIns.getTask_id());
            approvalFormVO.setProcessMemo(oIns.getProcess_memo());
        }
        approvalFormVO.setCmdList(cmdModelList);

        if (cmdModelList.size() == 0 && !approvalFormVO.getReadonly()) {
            //判断是否是退回到了开始节点，如果是则允许提交
            Boolean isStartNode = currentTaskEntity.getTask_type().equals(ETaskType.StartEvent.getType());
            Boolean unProcess = taskDao.getTinsById(oIns.getTins_id()).getProcess_status().equals(EProcessStatus.UnProcess.getStatus());
            approvalFormVO.setAllowSubmit(isStartNode && unProcess);
        }
        CustomPageSetting refPageSetting = getRefPageSetting(currentTaskEntity);
        if (refPageSetting != null && !StringUtils.isEmpty(refPageSetting.getNavCode())) {
            approvalFormVO.setCustomPage(refPageSetting);
        } else {
            CustomFormVO formVO = getCustomFormByOIns(approvalFormVO, oIns);
            approvalFormVO.setCustomForm(formVO);
            //非只读并且有表单任意字段编辑权限才允许显示保存按钮
            if (!approvalFormVO.getReadonly()) {
                approvalFormVO.setAllowSave(formVO.getTableInfo().getFieldList().stream().anyMatch(f -> f.getFieldAccess().equals(EAccessGrade.Write)));
            }
            //获取评分表单
            String ratingFormId = TypeConverterUtils.object2String(currentTaskEntity.get("rating_form"), "");
            if (!StringUtils.isEmpty(ratingFormId)) {
                CustomFormVO ratingFormVO = getRatingFormByOIns(ratingFormId, oIns.getUser_id(), formVO.getData().getId().toString());
                approvalFormVO.setRatingForm(ratingFormVO);
            }
            //获取评分汇总
            String ratingSumTable = TypeConverterUtils.object2String(currentTaskEntity.get("rating_sum"), "");
            if (!StringUtils.isEmpty(ratingSumTable)) {
                DbCollection ratingColl = getRatingList(ratingSumTable, formVO.getData().getId().toString());
                approvalFormVO.setRatingList(ratingColl);
            }
        }
        DbCollection collection = getApprovalList(oIns.getFins_id());
        approvalFormVO.setApprovalList(collection);
        return approvalFormVO;
    }

    private DbCollection getRatingList(String ratingSumTable, String dataId) {
        QueryOption queryOption = new QueryOption(ratingSumTable);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("data_id", dataId);
        queryOption.setTableFilter(filterWrapper.build());
        return dataAccess.query(queryOption);
    }

    @SneakyThrows
    private CustomFormVO getRatingFormByOIns(String rationFormId, String userId, String mainDataId) {
        if (StringUtils.isEmpty(rationFormId)) {
            return null;
        }
        String tableCode = getTableByFormId(rationFormId);
        DbTable table = dataAccess.initTableInfoByTableCode(tableCode);
        if (table.getFieldList().stream().noneMatch(f -> f.getCode().equals("data_id"))) {
            throw new WarnCommonException("评分表单主表必须包含data_id字段");
        }
        if (table.getFieldList().stream().noneMatch(f -> f.getCode().equals("user_id"))) {
            throw new WarnCommonException("评分表单主表必须包含user_id字段");
        }
        QueryOption queryOption = new QueryOption(tableCode);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("user_id", userId).eq("data_id", mainDataId);
        queryOption.setFixField("id");
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setFixField("user_id,data_id");
        DbCollection bisDataColl = dataAccess.queryAllData(queryOption);
        if (bisDataColl.getData().size() == 0) {
            DbEntity entity = bisDataColl.newEntity();
            entity.put("user_id", userId);
            entity.put("data_id", mainDataId);
            entity.setState(EDBEntityState.Added);
            bisDataColl.getData().add(entity);
            dataAccess.update(bisDataColl);
        }
        return formDataService.getEditDataByFormId(rationFormId, bisDataColl.getData().get(0).getId().toString(), null);
    }

    private String getTableByFormId(String formId) {
        QueryOption queryOption = new QueryOption("dm_custom_form");
        queryOption.setFixField("table_code");
        queryOption.setTableFilter(new TableFilter("id", formId));
        DbCollection coll = dataAccess.queryAllData(queryOption);
        if (coll.getData().size() == 0) {
            return null;
        }
        return coll.getData().get(0).get("table_code").toString();
    }

    private CustomPageSetting getRefPageSetting(WfTaskEntity taskEntity) {
        String taskSetting = TypeConverterUtils.object2String(taskEntity.get("ref_page_setting"), "");
        if (StringUtils.isEmpty(taskSetting)) {
            return null;
        }
        CustomPageSetting pageSetting = null;
        try {
            pageSetting = JSON.parseObject(taskSetting, CustomPageSetting.class);
        } catch (Exception exception) {
            log.error(String.format("任务节点%s转换taskSettingModel失败", taskEntity.getId()), exception);
        }
        return pageSetting;
    }

    @SneakyThrows
    @Override
    public WFApprovalFormVO getApprovalHistoryForm(@NotNull String fInsId, String oInsId) {
        WfFInsEntity fIns = fInsDao.getFinsById(fInsId);
        if (fIns == null) {
            throw new WarnCommonException("fInsId 不存在请联系管理员");
        }
        WFApprovalFormVO approvalFormVO = new WFApprovalFormVO();
        approvalFormVO.setReadonly(true);
        approvalFormVO.setAllowSave(false);
        CustomFormVO formVO = getCustomFormByFIns(fIns);
        formVO.getBtns().clear();
        approvalFormVO.setCustomForm(formVO);

        // 如果oInsId不为空，则获取流程实例信息
        if (!StringUtils.isEmpty(oInsId)) {
            WfOInsEntity oIns = operatorDao.getOInstanceById(oInsId);
            LoginUser user = appHolderService.getUser();
            //需要判断当前用户在查看下已办时可以看到评分表单，管理员在监控时无法看到
            if (user.getId().toString().equals(oIns.getUser_id())) {
                WfTaskEntity currentTaskEntity = taskDao.getTaskEntityById(oIns.getTask_id());
                //获取评分表单
                String ratingFormId = TypeConverterUtils.object2String(currentTaskEntity.get("rating_form"), "");
                if (!StringUtils.isEmpty(ratingFormId)) {
                    CustomFormVO ratingFormVO = getRatingFormByOIns(ratingFormId, oIns.getUser_id(), formVO.getData().getId().toString());
                    approvalFormVO.setRatingForm(ratingFormVO);
                }
                //获取评分汇总
                String ratingSumTable = TypeConverterUtils.object2String(currentTaskEntity.get("rating_sum"), "");
                if (!StringUtils.isEmpty(ratingSumTable)) {
                    DbCollection ratingColl = getRatingList(ratingSumTable, formVO.getData().getId().toString());
                    approvalFormVO.setRatingList(ratingColl);
                }
            }
        }
        DbCollection collection = getApprovalList(fInsId);
        approvalFormVO.setApprovalList(collection);
        return approvalFormVO;
    }


    List<WFCmdModel> getCmdList(String taskId) {
        List<WfCmdEntity> entityList = cmdDao.getCmdListByTaskId(taskId);
        List<WFCmdModel> cmdModelList = new ArrayList<>();
        for (WfCmdEntity entity : entityList) {
            WFCmdModel model = new WFCmdModel();
            model.setId(entity.getId().toString());
            model.setOrder_id(entity.getOrder_id());
            model.setCmd_des(entity.getCmd_des());
            model.setCmd_type(entity.getCmd_type());
            if (entity.getRequire_process_memo() != null && entity.getRequire_process_memo()) {
                model.setRequire_process_memo(1);
            } else {
                model.setRequire_process_memo(0);
            }
            cmdModelList.add(model);
        }
        return cmdModelList;
    }

    @SneakyThrows
    @Override
    public DbCollection getApprovalList(String fInsId) {
        if (StringUtils.isEmpty(fInsId)) {
            throw new WarnCommonException("待处理数据格式不正确");
        }
        QueryOption queryOption = new QueryOption(WfOInsEntity.TABLE_CODE);
        queryOption.setTableFilter(new TableFilter(WfOInsEntity.COL_FINS_ID, fInsId));
        queryOption.setFixField("task_name,user_name,start_time,process_time,process_status,process_cmd_des,process_memo");
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<DbEntity> newList = collection.getData().stream()
                .filter(o -> !o.get(WfOInsEntity.COL_PROCESS_STATUS).toString().equals(EProcessStatus.Skipped.getStatus()) &&
                        !o.get(WfOInsEntity.COL_PROCESS_STATUS).toString().equals(EProcessStatus.UnEnabled.getStatus())
                ).collect(Collectors.toList());
        collection.setData(newList);
        return collection;
    }


    @SneakyThrows
    CustomFormVO getCustomFormByOIns(WFApprovalFormVO approvalFormVO, WfOInsEntity oIns) {
        CustomFormVO formVO = null;
        WfFInsEntity fIns = fInsDao.getFinsById(oIns.getFins_id());
        if (fIns == null) {
            return formVO;
        }
        String formId = getRefFormId(fIns.getFlow_id());
        WfTaskEntity taskEntity = taskDao.getTaskEntityById(oIns.getTask_id());
        String dataId = fIns.getData_id();
        if (!StringUtils.isEmpty(formId) && !StringUtils.isEmpty(dataId)) {
            ClientMacro clientMacro = new ClientMacro();
            formVO = formDataService.getEditDataByFormId(formId, dataId, clientMacro);
        }
        if (formVO == null || formVO.getTableInfo() == null) {
            throw new WarnCommonException("流程表单配置不正确，请联系管理员配置");
        }
        List<WfTaskFieldModel> taskFieldModelList = taskService.getTaskCustomSetting(taskEntity);
        if (taskFieldModelList == null) {
            return formVO;
        }
        //设置表单字段可见属性
        List<DbField> taskFieldList = taskService.getTaskNodeFormField(formVO.getTableInfo().getFieldList(), taskFieldModelList);
        formVO.getTableInfo().setFieldList(taskFieldList);
        //设置子表显示和隐藏
        for (WfTaskFieldModel fieldModel : taskFieldModelList) {
            // 因为目前数控并没有保存elementType属性，所以为了兼容性判断下id长度识别是否是子表
            if (fieldModel.getId().length() < 20 && (fieldModel.getElementType() == null || !fieldModel.getElementType().equals(2))) {
                continue;
            }
            //把子表的serialNum=“”或null ，则前端不渲染此group
            if (fieldModel.getHidden() != null && fieldModel.getHidden().equals(1)) {
                formVO.setLayout(formVO.getLayout().replace(fieldModel.getId(), ""));
                removeFormButton(formVO, fieldModel.getId());
            } else if (fieldModel.getReadonly() != null && fieldModel.getReadonly().equals(1)) {
                removeFormButton(formVO, fieldModel.getId());
            }
        }
        return formVO;
    }

    void removeFormButton(CustomFormVO formVO, String groupFlag) {
        List<FormButtonVO> newListFormBtnList = new ArrayList<>();
        for (FormButtonVO buttonVO : formVO.getBtns()) {
            if (buttonVO.getSerialNum().equals(groupFlag)) {
                continue;
            }
            newListFormBtnList.add(buttonVO);
        }
        formVO.setBtns(newListFormBtnList);
    }

    @SneakyThrows
    String getRefFormId(String flowId) {
        DbCollection collFlow = dataAccess.queryById(WfFLowEntity.TABLE_CODE, Long.parseLong(flowId), WfFLowEntity.COL_FORM_ID);
        if (collFlow.getData().size() == 0) {
            throw new WarnCommonException("数据异常，请检查参数");
        }
        String formId = TypeConverterUtils.object2String(collFlow.getData().get(0).get(WfFLowEntity.COL_FORM_ID), "");
        if (StringUtils.isEmpty(formId)) {
            throw new WarnCommonException("未设置关联表单，无法获取可用字段，请设置");
        }
        return formId;
    }

    CustomFormVO getCustomFormByFIns(WfFInsEntity fIns) {
        CustomFormVO formVO = null;
        String formId = getRefFormId(fIns.getFlow_id());
        String dataId = fIns.getData_id();
        if (!StringUtils.isEmpty(formId) && !StringUtils.isEmpty(dataId)) {
            formVO = formDataService.getEditDataByFormId(formId, dataId, null);
        }
        return formVO;
    }

}
