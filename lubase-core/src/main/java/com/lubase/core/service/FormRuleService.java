package com.lubase.core.service;

import com.lubase.core.entity.DmFormFilterEntity;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.model.FormButtonVO;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.model.customForm.FormRule;

import java.util.List;

/**
 * 表单规则配置服务
 */
public interface FormRuleService {
    /**
     * 渲染表单时，根据表单id获取特殊列的渲染规则
     *
     * @param formId
     * @return
     */
    FormRule getFormRuleById(Long formId);

    /**
     * 获取表单字段的级联条件
     *
     * @param form_id
     * @param column_id
     * @return
     */
    DmFormFilterEntity getFormFieldFilter(String form_id, String column_id);


    /**
     * 根据主表id和子表id获取关联关系，如果有则返回，如果没有则返回null
     *
     * @param mainTableId  主表的id
     * @param childTableId 子表的id
     * @return
     */
    DmTableRelationEntity getTableRelation(String mainTableId, String childTableId);

    /**
     * 获取表单子表对象
     *
     * @param formId
     * @param serialNum
     * @return
     */
    ChildTableSetting getFormChildTable(String formId, String serialNum);

    /**
     * 获取表单按钮列表
     *
     * @param formId
     * @return
     */
    List<FormButtonVO> getFormButtonListById(String formId);

    /**
     * 根据表单id获取表单的主表id
     *
     * @param formId
     * @return
     */
    String getMainTableId(String formId);

    /**
     * 根据表单id和子表序列号获取子表id
     *
     * @param formId
     * @param serialNum
     * @return
     */
    String getChildTableId(String formId, String serialNum);
}
