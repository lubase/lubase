package com.lubase.wfengine.dao;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.wfengine.auto.entity.WfCmdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class WfCmdDao {
    @Autowired
    DataAccess dataAccess;

    public WfCmdEntity getWfCmdById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Long longId = 0l;
        try {
            longId = TypeConverterUtils.object2Long(id);
        } catch (Exception ex) {
            return null;
        }
        DbCollection collection = dataAccess.queryById(WfCmdEntity.TABLE_CODE, longId);
        List<WfCmdEntity> list = collection.getGenericData(WfCmdEntity.class);
        return list.stream().findFirst().orElse(null);
    }

    public List<WfCmdEntity> getCmdListByTaskId(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            return null;
        }
        QueryOption queryOption = new QueryOption(WfCmdEntity.TABLE_CODE);
        queryOption.setTableFilter(new TableFilter(WfCmdEntity.COL_TASK_ID, taskId));
        return dataAccess.queryAllData(queryOption).getGenericData(WfCmdEntity.class);
    }
}
