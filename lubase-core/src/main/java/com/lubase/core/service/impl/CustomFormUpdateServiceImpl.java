package com.lubase.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.service.FormRuleService;
import com.lubase.model.DbField;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.core.service.CustomFormUpdateService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomFormUpdateServiceImpl implements CustomFormUpdateService {

    private static final String FORM_TYPE_CUSTOM = "custom";
    /**
     * 表触发器传递的客户端表单标识key值
     */
    private static final String FORM_ID_FLAG = "__form_id";

    /**
     * 子表更新设置 属性
     */
    private static final String FORM_CHILD_TABLE_SETTING = "__child_table_setting";

    @Autowired
    DataAccess dataAccess;

    @Autowired
    CustomFormDataService customFormDataService;
    @Autowired
    FormRuleService formRuleService;

    @SneakyThrows
    @Override
    public Integer saveFormData(String formId, DbEntity dbEntity) {
        return saveFormData(formId, dbEntity, null);
    }

    @SneakyThrows
    Integer saveFormData(String formId, DbEntity dbEntity, DbField fkField) {
        if (StringUtils.isEmpty(formId)) {
            throw new ParameterNotFoundException(String.format("formId不能为空", formId));
        }
        DmCustomFormEntity dmCustomform = customFormDataService.selectById(formId);
        if (null == dmCustomform) {
            throw new WarnCommonException(String.format("表单%s不存在", formId));
        }
        //传递formId参数
        dbEntity.put(FORM_ID_FLAG, formId);
        DbCollection collection = dataAccess.getEmptyData(dmCustomform.getTable_code());
        collection.getData().add(dbEntity);
        collection.setClientMode();
        // 1、auto form直接调用 dataAccess.update 方法进行更新
        //    如果是 customform 需要表单字段的验证才可以
        if (dmCustomform.getForm_type().equals(FORM_TYPE_CUSTOM)) {
            collection.getTableInfo().setFieldList(customFormDataService.getFormFieldSetting(dmCustomform));
        } else if (StringUtils.isEmpty(dmCustomform.getForm_type())) {
            throw new InvokeCommonException("表单类型设置不正确，无法保存");
        }
        //2、调用dataAccess的更新方法，此方法会验证数据权限
        //判断此表单是否需要触发触发器
        if (TypeConverterUtils.object2Integer(dmCustomform.get("disable_table_trigger"), 0).equals(1)) {
            collection.setEnableTableTrigger(false);
        }
        //3、如果是子表则设置外键
        if (fkField != null) {
            Boolean existsFkField = false;
            for (DbField field : collection.getTableInfo().getFieldList()) {
                if (field.getCode().equals(fkField.getCode())) {
                    existsFkField = true;
                    field.setRight(fkField.getRight());
                    field.setVisible(fkField.getVisible());
                }
            }
            if (!existsFkField) {
                collection.getTableInfo().getFieldList().add(fkField);
            }
        }
        int cc = dataAccess.update(collection);
        return cc;
    }

    @SneakyThrows
    @Override
    public Integer saveChildTableFormData(String formId, DbEntity dbEntity) {
        if (!dbEntity.containsKey(FORM_CHILD_TABLE_SETTING) || StringUtils.isEmpty(dbEntity.get(FORM_CHILD_TABLE_SETTING))) {
            throw new WarnCommonException("处理失败，报文中缺失" + FORM_CHILD_TABLE_SETTING + "属性");
        }
        DbField fkField = null;
        try {
            JSONObject obj = JSON.parseObject(dbEntity.get(FORM_CHILD_TABLE_SETTING).toString(), JSONObject.class);
            // 用于获取到主子表关系后，做一次校验，暂时不实现  A
            String pId = obj.getString("pId");
            String clientFormId = obj.getString("formId");
            String serialNum = obj.getString("serialNum");

            String childTableId = formRuleService.getChildTableId(clientFormId, serialNum);
            String mainTableId = formRuleService.getMainTableId(clientFormId);
            if (StringUtils.isEmpty(mainTableId) || StringUtils.isEmpty(childTableId)) {
                throw new WarnCommonException("表单主从关系配置信息不正确，请检查 1");
            }
            DmTableRelationEntity relationEntity = formRuleService.getTableRelation(mainTableId, childTableId);
            if (relationEntity == null || StringUtils.isEmpty(relationEntity.getFk_column_code())) {
                throw new WarnCommonException("表单主从关系配置信息不正确，请检查 2");
            }
            dbEntity.put(relationEntity.getFk_column_code(), pId);

            DbCollection collection = dataAccess.getEmptyDataByTableId(Long.parseLong(childTableId));
            for (DbField field : collection.getTableInfo().getFieldList()) {
                if (field.getCode().equals(relationEntity.getFk_column_code())) {
                    field.setRight(4);
                    field.setVisible(4);
                    fkField = field;
                    break;
                }
            }
        } catch (Exception exception) {
            throw exception;
        }
        return saveFormData(formId, dbEntity, fkField);
    }
}
