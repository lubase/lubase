package com.lcp.qibao.service.button.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.EDBEntityState;
import com.lcp.qibao.auto.entity.DmTableRelationEntity;
import com.lcp.qibao.auto.entity.SsButtonEntity;
import com.lcp.qibao.service.FormRuleService;
import com.lcp.qibao.service.button.OndDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Button37ChildFormBatchSaveImpl implements OndDataService {
    @Override
    public String getButtonType() {
        return "37";
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
        return dataAccess.update(collection);
    }

}
