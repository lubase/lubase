package com.lubase.core.service;

import com.lubase.core.model.CustomFormVO;
import com.lubase.core.util.ClientMacro;
import com.lubase.model.DbEntity;

/**
 * 获取页面数据的服务
 *
 * @author A
 */
public interface PageDataService {
    /**
     * 根据功能代码获取新增的表单
     *
     * @param funcCode 按钮的功能代码
     * @return
     */
    CustomFormVO getAddDataByFuncCode(String pageId, String funcCode, ClientMacro clientMacro);

    CustomFormVO getAddDataByFormId(String pageId, String formId, ClientMacro clientMacro);

    /**
     * 根据功能代码获取表单修改的数据
     *
     * @param funcCode 按钮的功能代码
     * @param dataId   业务数据id
     * @return
     */
    CustomFormVO getEditDataByFuncCode(String pageId, String funcCode, String dataId, ClientMacro clientMacro);

    /**
     * 根据表单ID获取表单数据
     *
     * @param formId 表单id
     * @param dataId 业务数据id
     * @return
     */
    CustomFormVO getEditDataByFormId(String pageId, String formId, String dataId, ClientMacro clientMacro);

    /**
     * 根据功能代码复制数据
     *
     * @param funcCode
     * @param dataId
     * @return
     */
    CustomFormVO getCopyDataByFuncCode(String funcCode, String dataId);

    /**
     * 保存表单数据。只支持新增和修改
     *
     * @param funcCode
     * @param dbEntity
     * @return
     */
    int saveFormDataByFuncCode(String funcCode, DbEntity dbEntity);

    /**
     * 根据表单id 保存数据
     *
     * @param formId
     * @param dbEntity
     * @return
     */
    int saveFormDataByFormId(String pageId, String formId, DbEntity dbEntity);
}
