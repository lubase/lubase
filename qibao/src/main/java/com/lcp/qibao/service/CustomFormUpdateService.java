package com.lcp.qibao.service;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.qibao.auto.entity.DmCustomFormEntity;

import java.util.List;

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
}
