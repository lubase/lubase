package com.lcp.core;

import com.lcp.core.extend.ITableTrigger;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbTable;
import org.springframework.stereotype.Component;

@Component
public class FormTestTableTrigger implements ITableTrigger {
    @Override
    public String getTriggerTableCode() {
        return "form_test";
    }

    @Override
    public String getTriggerName() {
        return "form_test 表触发器";
    }

    @Override
    public Boolean isEdit() {
        return  true;
    }

    @Override
    public void afterUpdate(DbTable tableInfo, DbEntity entity, Boolean isServer) throws Exception {
        ITableTrigger.super.afterUpdate(tableInfo, entity, isServer);
    }
}
