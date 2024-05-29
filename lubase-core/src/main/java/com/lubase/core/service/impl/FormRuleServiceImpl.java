package com.lubase.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.core.model.FormButtonVO;
import com.lubase.core.model.customForm.*;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.entity.DmFormFilterEntity;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.service.FormRuleService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FormRuleServiceImpl implements FormRuleService {

    @Autowired
    DataAccess dataAccess;
    @Autowired
    @Qualifier("registerColumnInfoServiceApi")
    RegisterColumnInfoService registerColumnInfoService;

    @Override
    public FormRule getFormRuleById(Long formId) {
        if (formId == 0L) {
            return null;
        }
        //字段级联过滤
        QueryOption queryOption = new QueryOption("dm_form_filter");
        queryOption.setFixField("column_id,rely_column");
        queryOption.setTableFilter(new TableFilter("form_id", formId, EOperateMode.Equals));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        FormRule formRule = new FormRule();
        List<FieldRelyModel> fieldRelyModelList = new ArrayList<>();
        for (DbEntity entity : collection.getData()) {
            FieldRelyModel fieldRelyModel = new FieldRelyModel();
            fieldRelyModel.setField(entity.get("column_id").toString());
            fieldRelyModel.setRelyFields(entity.get("rely_column").toString());
            fieldRelyModelList.add(fieldRelyModel);
        }
        formRule.setFieldRely(fieldRelyModelList);
        //字段级联控制
        queryOption = new QueryOption("dm_form_control");
        queryOption.setFixField("column_id,control_rule");
        queryOption.setTableFilter(new TableFilter("form_id", formId, EOperateMode.Equals));
        collection = dataAccess.queryAllData(queryOption);
        List<FieldControlModel> fieldControlModelList = new ArrayList<>();
        for (DbEntity entity : collection.getData()) {
            FieldControlModel fieldControlModel = new FieldControlModel();
            fieldControlModel.setField(entity.get("column_id").toString());
            fieldControlModel.setControlRules(entity.get("control_rule").toString());
            fieldControlModelList.add(fieldControlModel);
        }
        formRule.setFieldControl(fieldControlModelList);
        //回填规则
        getBackFileRules(formRule, formId);
        return formRule;
    }

    private void getBackFileRules(FormRule formRule, Long formId) {
        var queryOption = new QueryOption("dm_form_backfill");
        queryOption.setFixField("column_id,control_rule");
        queryOption.setTableFilter(new TableFilter("form_id", formId, EOperateMode.Equals));
        var collection = dataAccess.queryAllData(queryOption);
        List<FieldBackfillModel> fieldBackfillModelList = new ArrayList<>();
        for (DbEntity entity : collection.getData()) {
            var fieldControlModel = new FieldBackfillModel();
            fieldControlModel.setField(entity.get("column_id").toString());
            fieldControlModel.setBackfillRules(entity.get("control_rule").toString());
            fieldBackfillModelList.add(fieldControlModel);
        }
        formRule.setFieldBackfill(fieldBackfillModelList);
    }

    @Override
    public DmFormFilterEntity getFormFieldFilter(String form_id, String column_id) {
        QueryOption queryOption = new QueryOption(DmFormFilterEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and().eq(DmFormFilterEntity.COL_FORM_ID, form_id)
                .eq(DmFormFilterEntity.COL_COLUMN_ID, column_id);
        queryOption.setTableFilter(filterWrapper.build());
        List<DmFormFilterEntity> list = dataAccess.query(queryOption).getGenericData(DmFormFilterEntity.class);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @SneakyThrows
    @Override
    public DmTableRelationEntity getTableRelation(String mainTableId, String childTableId) {
        if (StringUtils.isEmpty(mainTableId) || StringUtils.isEmpty(childTableId)) {
            throw new ParameterNotFoundException("mainTableId and childTableId not null");
        }
        QueryOption queryOption = new QueryOption(DmTableRelationEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(DmTableRelationEntity.COL_MAIN_TABLE_ID, mainTableId).eq(DmTableRelationEntity.COL_CHILD_TABLE_ID, childTableId);
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        if (collection.getData().size() > 1) {
            throw new WarnCommonException("检索到多条数据，请检查配置");
        } else if (collection.getData().size() == 1) {
            return collection.getGenericData(DmTableRelationEntity.class).get(0);
        } else {
            return null;
        }
    }

    @SneakyThrows
    @Override
    public ChildTableSetting getFormChildTable(String formId, String serialNum) {
        DbCollection collection = dataAccess.queryById(DmCustomFormEntity.TABLE_CODE, Long.parseLong(formId));
        if (collection.getTotalCount() != 1) {
            throw new InvokeCommonException("formId 参数异常");
        }
        DmCustomFormEntity formEntity = collection.getGenericData(DmCustomFormEntity.class).get(0);
        Long mainTableId = formEntity.getTable_id();
        //2、获取表单主表与子表关联关系
        ChildTableSetting childTable = null;
        try {
            List<ChildTableSetting> childTables = JSONObject.parseArray(formEntity.getChild_table(), ChildTableSetting.class);
            for (ChildTableSetting childTableSetting : childTables) {
                if (childTableSetting.getSerialNum().equals(serialNum)) {
                    childTable = childTableSetting;
                }
            }
        } catch (Exception exception) {
            //log.warn("子表%s未在表单配置中找到", serialNum);
        }
        return childTable;
    }

    @Override
    public List<FormButtonVO> getFormButtonListById(String formId) {
        QueryOption queryOption = new QueryOption("dm_form_button");
        queryOption.setTableFilter(new TableFilter("form_id", formId));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<FormButtonVO> buttonVOS = new ArrayList<>();
        for (DbEntity entity : collection.getData()) {
            FormButtonVO buttonVO = new FormButtonVO();
            buttonVO.setId(entity.getId());
            buttonVO.setCode(entity.getId().toString());
            buttonVO.setName(TypeConverterUtils.object2String(entity.get("button_name")));
            buttonVO.setBtnType(TypeConverterUtils.object2String(entity.get("button_type")));
            buttonVO.setDisType(TypeConverterUtils.object2String(entity.get("display_type")));
            buttonVO.setOrderId(TypeConverterUtils.object2Integer(entity.get("order_id")));
            buttonVO.setGroupDes(TypeConverterUtils.object2String(entity.get("group_des")));
            //以下是否是客户端渲染参数
            buttonVO.setSerialNum(TypeConverterUtils.object2String(entity.get("serial_num")));
            buttonVO.setRenderSetting(TypeConverterUtils.object2String(entity.get("render_setting")));
            buttonVOS.add(buttonVO);
        }
        return buttonVOS;
    }

    @SneakyThrows
    @Override
    public String getMainTableId(String formId) {
        DbCollection collection = dataAccess.queryById(DmCustomFormEntity.TABLE_CODE, Long.parseLong(formId));
        if (collection.getTotalCount() != 1) {
            throw new InvokeCommonException("formId 参数异常");
        }
        String tableCode = collection.getData().get(0).get(DmCustomFormEntity.COL_TABLE_CODE).toString();
        return registerColumnInfoService.getTableIdByTableCode(tableCode);
    }

    @SneakyThrows
    @Override
    public String getChildTableId(String formId, String serialNum) {
        ChildTableSetting childTableSetting = getFormChildTable(formId, serialNum);
        if (childTableSetting == null || childTableSetting.getQueryOption() == null || StringUtils.isEmpty(childTableSetting.getQueryOption().getTableName())) {
            throw new WarnCommonException("表单子表配置信息错误，不能使用删除按钮");
        }
        String childTableId = registerColumnInfoService.getTableIdByTableCode(childTableSetting.getQueryOption().getTableName());
        return childTableId;
    }
}
