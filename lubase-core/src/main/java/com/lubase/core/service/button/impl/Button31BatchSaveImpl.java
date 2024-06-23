package com.lubase.core.service.button.impl;

import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.service.AppFuncDataService;
import com.lubase.core.service.button.MoreDataService;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
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
}
