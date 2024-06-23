package com.lubase.core.service;

import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.model.DbField;

import java.util.List;

public interface CustomFormDataService {
    /**
     * 获取表单
     *
     * @param formId
     * @return
     */
    DmCustomFormEntity selectById(String formId);

    /**
     * 获取表单中子表设置信息
     *
     * @param childTableStr childTableStr
     * @param serialNum 子表分组码
     * @return
     */
    ChildTableSetting getChildTableFromSettingStr(String childTableStr, String serialNum);

    /**
     * 获取表单字段设置
     *
     * @param dmCustomForm
     * @return
     */
    List<DbField> getFormFieldSetting(DmCustomFormEntity dmCustomForm);

    /**
     * 把表单字段设置合并到表单默认字段中
     *
     * @param allFieldList
     * @param fieldInfo
     * @return
     */
    List<DbField> getFormFieldSetting(List<DbField> allFieldList, String fieldInfo);
}
