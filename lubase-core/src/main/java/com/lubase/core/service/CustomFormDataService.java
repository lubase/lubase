package com.lubase.core.service;

import com.lubase.model.DbField;
import com.lubase.core.entity.DmCustomFormEntity;

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
     * 获取表单字段设置
     *
     * @param dmCustomForm
     * @return
     */
    List<DbField> getFormFieldSetting(DmCustomFormEntity dmCustomForm);
}
