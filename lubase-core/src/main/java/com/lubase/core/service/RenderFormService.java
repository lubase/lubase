package com.lubase.core.service;

import com.lubase.core.model.ColumnRefPageVO;
import com.lubase.core.model.CustomFormVO;
import com.lubase.core.model.customForm.ColumnLookupInfoVO;
import com.lubase.core.model.customForm.ColumnLookupParamModel;
import com.lubase.core.util.ClientMacro;

/**
 * <p>
 * 详情页面数据
 * </p>
 *
 * @author bluesky
 * @jdk 1.8
 */
public interface RenderFormService {

    CustomFormVO getAddDataByFormId(String formId, ClientMacro clientMacro);

    /**
     * 根据表单ID获取表单数据
     *
     * @param formId
     * @param dataId
     * @return
     */
    CustomFormVO getEditDataByFormId(String formId, String dataId, ClientMacro clientMacro);

    /**
     * 根据功能代码复制数据
     *
     * @param formId
     * @param dataId
     * @return
     */
    CustomFormVO getCopyDataByFormId(String formId, String dataId);

    /**
     * 获取lookup字段关联的信息
     *
     * @param columnLookupParam
     */
    ColumnLookupInfoVO getColLookupData(ColumnLookupParamModel columnLookupParam);

    /**
     * getColServiceData
     *
     * @param columnServiceParam
     */
    ColumnLookupInfoVO getColServiceData(ColumnLookupParamModel columnServiceParam);

    /**
     * @param columnId
     */
    ColumnRefPageVO getColRefPageInfo(String columnId);

    /**
     * 检查列值是否唯一
     *
     * @param columnId
     * @param val
     * @return
     */
    Boolean checkFieldUniqueValue(String columnId, String val, String currentDataId);
}
