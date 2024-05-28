package com.lubase.starter.service.button.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.starter.auto.entity.DmTableRelationEntity;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.service.FormRuleService;
import com.lubase.starter.service.button.OndDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Button23ChildFormAddDataImpl implements OndDataService {
    @Override
    public String getButtonType() {
        return "23";
    }

    @Override
    public String getDescription() {
        return "表单子表新增数据";
    }

    @Autowired
    FormRuleService formRuleService;

    @Autowired
    DataAccess dataAccess;

    @Override
    public Object exe(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        String jsonStr = checkAndGetParam("jsonStr", mapParam);
        // 用于获取到主子表关系后，做一次校验，暂时不实现  A
        String pId = checkAndGetParam("pId", mapParam);
        String formId = checkAndGetParam("formId", mapParam);
        String serialNum = checkAndGetParam("serialNum", mapParam);

        String childTableId = formRuleService.getChildTableId(formId, serialNum);
        String mainTableId = formRuleService.getMainTableId(formId);
        if (StringUtils.isEmpty(mainTableId) || StringUtils.isEmpty(childTableId)) {
            throw new WarnCommonException("表单主从关系配置信息不正确，请检查 1");
        }
        DmTableRelationEntity relationEntity = formRuleService.getTableRelation(mainTableId, childTableId);
        if (relationEntity == null || StringUtils.isEmpty(relationEntity.getFk_column_code())) {
            throw new WarnCommonException("表单主从关系配置信息不正确，请检查 2");
        }
        DbEntity entity = JSON.parseObject(jsonStr, DbEntity.class);
        if (entity == null) {
            throw new WarnCommonException("表单数据不正确");
        }
        entity.setState(EDBEntityState.Added);
        entity.put(relationEntity.getFk_column_code(), pId);

        DbCollection collection = dataAccess.getEmptyDataByTableId(Long.parseLong(childTableId));
        for (DbField field : collection.getTableInfo().getFieldList()) {
            if (field.getCode().equals(relationEntity.getFk_column_code())) {
                field.setRight(4);
                field.setVisible(4);
            }
        }
        collection.getData().add(entity);
        return dataAccess.update(collection);
    }

}
