package com.lubase.core.service.button.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.service.FormRuleService;
import com.lubase.core.service.button.OndDataService;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Button32ChildFormBatchSaveImpl implements OndDataService {
    @Override
    public String getButtonType() {
        return "32";
    }

    @Override
    public String getDescription() {
        return "表单子表批量保存数据";
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
        DbEntity[] entities = JSON.parseObject(jsonStr, DbEntity[].class);
        if (entities == null || entities.length == 0) {
            return 1;
        }

        String childTableId = formRuleService.getChildTableId(formId, serialNum);
        String mainTableId = formRuleService.getMainTableId(formId);
        if (StringUtils.isEmpty(mainTableId) || StringUtils.isEmpty(childTableId)) {
            throw new WarnCommonException("表单主从关系配置信息不正确，请检查 1");
        }
        DmTableRelationEntity relationEntity = formRuleService.getTableRelation(mainTableId, childTableId);
        if (relationEntity == null || StringUtils.isEmpty(relationEntity.getFk_column_code())) {
            throw new WarnCommonException("表单主从关系配置信息不正确，请检查 2");
        }
        DbCollection collection = dataAccess.getEmptyDataByTableId(Long.parseLong(childTableId));
        for (DbEntity entity : entities) {
            entity.put(relationEntity.getFk_column_code(), pId);
            entity.acceptChange();
            entity.setState(EDBEntityState.Modified);
            collection.getData().add(entity);
        }
        collection.setClientMode();
        return dataAccess.update(collection);
    }

}
