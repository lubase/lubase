package com.lubase.starter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbField;
import com.lubase.model.EAccessGrade;
import com.lubase.starter.model.customForm.DbFieldExtend;
import com.lubase.starter.model.customForm.FormGroup;
import com.lubase.starter.model.customForm.FormTab;
import com.lubase.starter.model.customForm.Tabs;
import com.lubase.starter.auto.entity.DmCustomFormEntity;
import com.lubase.starter.service.CustomFormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<DbField> getFormFieldSetting(DmCustomFormEntity dmCustomForm) {
        QueryOption queryOption = new QueryOption(dmCustomForm.getTable_code());
        queryOption.setFixField(dmCustomForm.getCols());
        DbCollection collection = dataAccess.queryFieldList(queryOption);
        List<DbField> allFieldList = collection.getTableInfo().getFieldList();
        //表单更新时 表单字段设置的编辑属性 以表单内为主
        Map<String, DbFieldExtend> formField = getFormField(dmCustomForm);
        //设置表单资源编辑属性
        mergeCustomFormToRegisterFieldInfo(allFieldList, formField);
        return allFieldList;
    }

    Map<String, DbFieldExtend> getFormField(DmCustomFormEntity dmCustomForm) {
        //表单更新时 表单字段设置的编辑属性 以表单内为主
        Map<String, DbFieldExtend> formField = new HashMap<>();
        Tabs tabs = JSONObject.parseObject(dmCustomForm.getData(), Tabs.class);
        for (FormTab formTab : tabs.getTabs()) {
            if (formTab == null || formTab.getGroups() == null) {
                continue;
            }
            for (FormGroup group : formTab.getGroups()) {
                if (group == null || group.getFieldList() == null) {
                    continue;
                }
                List<DbFieldExtend> fieldList = group.getFieldList();
                for (DbFieldExtend fieldExtend : fieldList) {
                    if (!formField.containsKey(fieldExtend.getId())) {
                        formField.put(fieldExtend.getId(), fieldExtend);
                    }
                }
            }
        }
        return formField;
    }

    List<DbField> mergeCustomFormToRegisterFieldInfo(List<DbField> registerFieldList, Map<String, DbFieldExtend> mapCustomFormField) {
        //设置表单资源编辑属性
        if (mapCustomFormField.size() > 0) {
            for (DbField field : registerFieldList) {
                if (mapCustomFormField.containsKey(field.getId())) {
                    field.setVisible(mapCustomFormField.get(field.getId()).getFieldAccess());
                    field.setIsNull(mapCustomFormField.get(field.getId()).getIsNull() ? 1 : 0);
                    //表单字段可编辑字段默认均不再处理
                    if (field.getFieldAccess().getIndex() > EAccessGrade.Read.getIndex()) {
                        field.setColDefault("");
                    }
                } else {
                    //表单外字段不更新
                    field.setVisible(0);
                }

            }
        }
        return registerFieldList;
    }
}
