package com.lubase.core.service;

import com.lubase.core.model.ColumnRefPageVO;
import com.lubase.core.model.CustomFormVO;
import com.lubase.core.model.customForm.ChildTableDataVO;
import com.lubase.core.model.customForm.ColumnLookupInfoVO;
import com.lubase.core.model.customForm.ColumnLookupParamModel;
import com.lubase.core.util.ClientMacro;
import com.lubase.orm.exception.ParameterNotFoundException;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.HashMap;

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

    /**
     * 获取表单 子表数据
     *
     * @param mapParam
     * @return
     */
    ChildTableDataVO getFormChildTableData(HashMap<String, String> mapParam);

    /**
     * 获取表单 子表所有数据
     *
     * @param mapParam
     * @return
     */
    ChildTableDataVO getFormChildAllTableData(HashMap<String, String> mapParam);

    /**
     * 检查参数是否存在，如果不存在会抛出ParameterNotFoundException
     *
     * @param key
     * @param mapParam
     */
    @SneakyThrows
    default String checkAndGetParam(String key, HashMap<String, String> mapParam) {
        if (mapParam.containsKey(key) && !StringUtils.isEmpty(mapParam.get(key))) {
            return mapParam.get(key);
        } else {
            throw new ParameterNotFoundException(key);
        }
    }
}
