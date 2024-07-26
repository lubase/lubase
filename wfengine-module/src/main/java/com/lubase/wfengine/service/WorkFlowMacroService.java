package com.lubase.wfengine.service;

import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 流程宏服务
 */
@Component
public class WorkFlowMacroService {
    /**
     * 流程宏前缀
     */
    public static final String workFlowMacroPre = "@@WF.";


    @Autowired
    DataAccess dataAccess;

    public DbEntity getWorkFlowMacro(String finsId) {
        if (StringUtils.isEmpty(finsId)) {
            return new DbEntity();
        }
        QueryOption queryOption = new QueryOption("wf_fins");
        queryOption.setFixField("c_task_id,c_task_name,process_user_id");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("id", finsId);
        queryOption.setTableFilter(filterWrapper.build());
        if (dataAccess.query(queryOption).getData().size() > 0) {
            DbEntity entity = dataAccess.query(queryOption).getData().get(0);
            DbEntity newEntity = new DbEntity();
            for (String key : entity.keySet()) {
                newEntity.putWithNoTrace(workFlowMacroPre + key, entity.get(key));
            }
            return newEntity;
        }
        return new DbEntity();
    }
}
