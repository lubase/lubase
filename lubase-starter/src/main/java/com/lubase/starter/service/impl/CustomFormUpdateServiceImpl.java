package com.lubase.starter.service.impl;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.exception.ParameterNotFoundException;
import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.starter.auto.entity.DmCustomFormEntity;
import com.lubase.starter.service.CustomFormDataService;
import com.lubase.starter.service.CustomFormUpdateService;
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

    @Autowired
    DataAccess dataAccess;

    @Autowired
    CustomFormDataService customFormDataService;

    @SneakyThrows
    @Override
    public Integer saveFormData(String formId, DbEntity dbEntity) {
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
        int cc = dataAccess.update(collection);
        return cc;
    }
}
