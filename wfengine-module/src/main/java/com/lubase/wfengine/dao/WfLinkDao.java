package com.lubase.wfengine.dao;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.wfengine.auto.entity.WfLinkEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class WfLinkDao {

    @Autowired
    DataAccess dataAccess;

    public List<WfLinkEntity> getNextLinkForTask(String flowId, String taskId, String cmdId) {
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(WfLinkEntity.COL_BEGIN_TASK_ID, taskId);
        if (!StringUtils.isEmpty(cmdId)) {
            filterWrapper.eq(WfLinkEntity.COL_CMD_ID, cmdId);
        }
        filterWrapper.eq(WfLinkEntity.COL_FLOW_ID, flowId);
        QueryOption queryOption = new QueryOption(WfLinkEntity.TABLE_CODE);
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<WfLinkEntity> list = collection.getGenericData(WfLinkEntity.class);
        return list;
    }
}
