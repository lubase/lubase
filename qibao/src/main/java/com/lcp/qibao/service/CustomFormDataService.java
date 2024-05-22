package com.lcp.qibao.service;

import com.lcp.coremodel.DbField;
import com.lcp.qibao.auto.entity.DmCustomFormEntity;

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
