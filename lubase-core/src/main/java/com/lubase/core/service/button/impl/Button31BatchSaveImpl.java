package com.lubase.core.service.button.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.service.AppFuncDataService;
import com.lubase.core.service.FormRuleService;
import com.lubase.core.service.button.MoreDataService;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class Button31BatchSaveImpl implements MoreDataService {

    @Autowired
    AppFuncDataService appFuncDataService;

    @Autowired
    DataAccess dataAccess;
    @Autowired
    FormRuleService formRuleService;

    @Override
    public String getButtonType() {
        return "31";
    }

    @Override
    public String getDescription() {
        return "主表批量保存按钮";
    }

    @Override
    public Object exe(SsButtonEntity button, List<HashMap<String, String>> listMapParam) throws Exception {
        boolean isCustomForm = button.containsKey("serial_num") && !StringUtils.isEmpty(button.get("serial_num").toString());
        if(isCustomForm){
            return customForm(button, listMapParam.get(0));
        }
        else {
            return listPage(button, listMapParam);
        }
    }

    Integer listPage(SsButtonEntity button, List<HashMap<String, String>> listMapParam) throws Exception {
        SsPageEntity pageEntity = appFuncDataService.getPageById(button.getPage_id().toString());
        // 从gridInfo中解析可编辑列
        String editColumns = appFuncDataService.getPageCanEditColumn(pageEntity);
        String mainTable = appFuncDataService.getPageRefTable(pageEntity);
        if (StringUtils.isEmpty(editColumns) || StringUtils.isEmpty(mainTable)) {
            throw new WarnCommonException("请检查页面配置信息");
        }
        QueryOption queryOption = new QueryOption(mainTable);
        queryOption.setFixField(editColumns);
        DbCollection coll = dataAccess.queryFieldList(queryOption);
        coll.setData(new ArrayList<>());
        for (HashMap<String, String> mapParam : listMapParam) {
            DbEntity entity = new DbEntity();
            entity.putAll(mapParam);
            if (entity.getId() != null) {
                entity.setState(EDBEntityState.Modified);
                coll.getData().add(entity);
            }
        }
        if (coll.getData().isEmpty()) {
            return 1;
        }
        coll.setClientMode();
        return dataAccess.update(coll);
    }

    Integer customForm(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(mainTableId) || org.apache.commons.lang3.StringUtils.isEmpty(childTableId)) {
            throw new WarnCommonException("表单主从关系配置信息不正确，请检查 1");
        }
        DmTableRelationEntity relationEntity = formRuleService.getTableRelation(mainTableId, childTableId);
        if (relationEntity == null || org.apache.commons.lang3.StringUtils.isEmpty(relationEntity.getFk_column_code())) {
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

    @SneakyThrows
    String checkAndGetParam(String key, HashMap<String, String> mapParam) {
        if (mapParam.containsKey(key) && !StringUtils.isEmpty(mapParam.get(key))) {
            return mapParam.get(key);
        } else {
            throw new ParameterNotFoundException(key);
        }
    }
}
