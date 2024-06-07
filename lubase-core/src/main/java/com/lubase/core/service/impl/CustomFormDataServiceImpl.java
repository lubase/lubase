package com.lubase.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.model.customForm.FormFieldInfo;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.model.DbField;
import com.lubase.model.EAccessGrade;
import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CustomFormDataServiceImpl implements CustomFormDataService {

    @Autowired
    DataAccess dataAccess;

    @Override
    public DmCustomFormEntity selectById(String formId) {
        if (StringUtils.isEmpty(formId)) {
            return null;
        }
        Long id = Long.valueOf(formId);
        DbCollection collection = dataAccess.queryById(DmCustomFormEntity.TABLE_CODE, id);
        if (collection.getTotalCount() == 1) {
            return collection.getGenericData(DmCustomFormEntity.class).get(0);
        }
        return null;
    }

    @Override
    public ChildTableSetting getChildTableFromSettingStr(String childTableStr, String serialNum) {
        if (StringUtils.isEmpty(childTableStr)) {
            return null;
        }
        ChildTableSetting childTable = null;
        try {
            List<ChildTableSetting> childTables = JSONObject.parseArray(childTableStr, ChildTableSetting.class);
            for (ChildTableSetting childTableSetting : childTables) {
                if (childTableSetting.getSerialNum().equals(serialNum)) {
                    childTable = childTableSetting;
                }
            }
        } catch (Exception exception) {
            log.error("获取表单子表配置错误" + serialNum, exception);
            throw exception;
        }
        return childTable;
    }

    @Override
    public List<DbField> getFormFieldSetting(DmCustomFormEntity dmCustomForm) {
        QueryOption queryOption = new QueryOption(dmCustomForm.getTable_code());
        queryOption.setFixField(dmCustomForm.getCols());
        DbCollection collection = dataAccess.queryFieldList(queryOption);
        List<DbField> allFieldList = collection.getTableInfo().getFieldList();
        //表单更新时 表单字段设置的编辑属性 以表单内为主
        Map<String, FormFieldInfo> formField = getFormField(dmCustomForm.getField_info());
        //设置表单资源编辑属性
        mergeCustomFormToRegisterFieldInfo(allFieldList, formField);
        return allFieldList;
    }

    @Override
    public List<DbField> getFormFieldSetting(List<DbField> allFieldList, String fieldInfo) {
        //表单更新时 表单字段设置的编辑属性 以表单内为主
        Map<String, FormFieldInfo> formField = getFormField(fieldInfo);
        //设置表单资源编辑属性
        mergeCustomFormToRegisterFieldInfo(allFieldList, formField);
        return allFieldList;
    }

    @SneakyThrows
    Map<String, FormFieldInfo> getFormField(String fieldInfoStr) {
        //表单更新时 表单字段设置的编辑属性 以表单内为主
        // [{"id":"2233234234","required":1,"readonly":0}]
        Map<String, FormFieldInfo> formFieldMap = new HashMap<>();
        if (StringUtils.isEmpty(fieldInfoStr)) {
            return formFieldMap;
        }
        List<FormFieldInfo> fieldInfoList = null;
        try {
            fieldInfoList = JSON.parseArray(fieldInfoStr, FormFieldInfo.class);
        } catch (Exception ex) {
            throw new InvokeCommonException("表单数据格式不正确，请联系管理员");
        }
        for (FormFieldInfo fieldInfo : fieldInfoList) {
            formFieldMap.put(fieldInfo.getId(), fieldInfo);
        }
        return formFieldMap;
    }

    List<DbField> mergeCustomFormToRegisterFieldInfo(List<DbField> registerFieldList, Map<String, FormFieldInfo> mapCustomFormField) {
        //设置表单资源编辑属性
        if (mapCustomFormField.size() == 0) {
            return registerFieldList;
        }
        for (DbField field : registerFieldList) {
            if (mapCustomFormField.containsKey(field.getId())) {
                FormFieldInfo formFieldSetting = mapCustomFormField.get(field.getId());
                // 注意此处表单的字段设置只会让字段编辑的设置更加严格，所以只需要判断是否设置了只读和必填，只有这两种情况下需要覆盖字段编辑设置
                if (formFieldSetting.getReadonly().equals(1)) {
                    field.setVisible(EAccessGrade.Read.getIndex());
                }
                if (formFieldSetting.getRequired().equals(1)) {
                    field.setIsNull(0);
                }
            }
        }
        return registerFieldList;
    }
}
