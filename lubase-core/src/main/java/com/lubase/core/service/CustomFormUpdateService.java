package com.lubase.core.service;

import com.lubase.model.DbEntity;

/**
 * 自定义表单更新服务
 */
public interface CustomFormUpdateService {
    /**
     * 保存表单数据。只支持新增和修改
     *
     * @param formId
     * @param dbEntity
     * @return
     */
    Integer saveFormData(String formId, DbEntity dbEntity);

    /**
     * 表单子表保存数据
     *
     * @param formId
     * @param dbEntity
     * @return
     */
    Integer saveChildTableFormData(String formId, DbEntity dbEntity);
}
