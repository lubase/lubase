package com.lubase.core.service.button.impl;

import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.EDBEntityState;
import com.lubase.core.exception.DataNotFoundException;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.service.FormRuleService;
import com.lubase.core.service.button.OndDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Button24FormDeleteImpl implements OndDataService {
    @Override
    public String getButtonType() {
        return "24";
    }

    @Override
    public String getDescription() {
        return "表单子表删除按钮";
    }

    @Autowired
    FormRuleService formRuleService;

    @Autowired
    DataAccess dataAccess;

    @Override
    public Object exe(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        String id = checkAndGetParam("id", mapParam);
        // 用于获取到主子表关系后，做一次校验，暂时不实现  A
        String pId = checkAndGetParam("pId", mapParam);
        String formId = checkAndGetParam("formId", mapParam);
        String serialNum = checkAndGetParam("serialNum", mapParam);
        ChildTableSetting childTableSetting = formRuleService.getFormChildTable(formId, serialNum);
        if (childTableSetting == null || childTableSetting.getQueryOption() == null || StringUtils.isEmpty(childTableSetting.getQueryOption().getTableName())) {
            throw new WarnCommonException("表单子表配置信息错误，不能使用删除按钮");
        }
        String mainTableCode = childTableSetting.getQueryOption().getTableName();

        DbCollection collection = dataAccess.queryById(mainTableCode, Long.parseLong(id), idHandle);
        if (collection.getTotalCount() != 1) {
            throw new DataNotFoundException(mainTableCode, id);
        }
        collection.getData().get(0).setState(EDBEntityState.Deleted);
        return dataAccess.update(collection);
    }
}
