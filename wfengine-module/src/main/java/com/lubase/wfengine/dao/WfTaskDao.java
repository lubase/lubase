package com.lubase.wfengine.dao;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.model.EProcessStatus;
import com.lubase.wfengine.model.ETaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class WfTaskDao {
    @Autowired
    DataAccess dataAccess;

    /**
     * 获取流程的开始节点
     *
     * @param flowId
     * @return
     */
    public WfTaskEntity getStartTask(String flowId) {
        QueryOption queryOption = new QueryOption(WfTaskEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(WfTaskEntity.COL_FLOW_ID, flowId).eq(WfTaskEntity.COL_TASK_TYPE, ETaskType.StartEvent.getType());
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<WfTaskEntity> list = collection.getGenericData(WfTaskEntity.class);
        return list.stream().findFirst().orElse(null);
    }

    public List<WfTaskEntity> getTaskByIds(String[] ids) {
        QueryOption queryOption = new QueryOption(WfTaskEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        if (ids.length == 0) {
            return new ArrayList<>();
        } else if (ids.length == 1) {
            filterWrapper.eq(WfTaskEntity.COL_ID, ids[0]);
        } else {
            filterWrapper.in(WfTaskEntity.COL_ID, String.join(",", ids));
        }
        queryOption.setTableFilter(filterWrapper.build());
        return dataAccess.queryAllData(queryOption).getGenericData(WfTaskEntity.class);
    }

    /**
     * 根据id获取任务节点
     *
     * @param id
     * @return
     */
    public WfTaskEntity getTaskEntityById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Long longId = 0l;
        try {
            longId = TypeConverterUtils.object2Long(id);
        } catch (Exception ex) {
            return null;
        }
        DbCollection collection = dataAccess.queryById(WfTaskEntity.TABLE_CODE, longId);
        List<WfTaskEntity> list = collection.getGenericData(WfTaskEntity.class);
        return list.stream().findFirst().orElse(null);
    }

    public Integer updateTInsStatus(Long id) {
        DbCollection collection = dataAccess.queryById(WfTInsEntity.TABLE_CODE, id);
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            entity.put(WfTInsEntity.COL_PROCESS_STATUS, EProcessStatus.Processed.getStatus());
            entity.put(WfTInsEntity.COL_PROCESS_TIME, LocalDateTime.now());
            return dataAccess.update(collection);
        } else {
            return 0;
        }
    }

    public WfTInsEntity createTaskInsWithProcessed(WfFInsEntity fins, WfTaskEntity taskEntity) {
        return createTaskIns(fins, taskEntity, null, true);
    }

    /**
     * 创建下一个任务节点实例
     *
     * @param fins
     * @param taskEntity
     * @return
     */
    public WfTInsEntity createTaskIns(WfFInsEntity fins, WfTaskEntity taskEntity, WfTaskEntity preTaskEntity) {
        Boolean processed = false;
        if (taskEntity.getTask_type().equals(ETaskType.EndEvent.getType())) {
            processed = true;
        }
        return createTaskIns(fins, taskEntity, preTaskEntity, processed);
    }

    WfTInsEntity createTaskIns(WfFInsEntity fins, WfTaskEntity taskEntity, WfTaskEntity preTaskEntity, Boolean processed) {
        DbCollection collection = dataAccess.getEmptyData(WfTInsEntity.TABLE_CODE);
        WfTInsEntity tIns = new WfTInsEntity();
        tIns.setState(EDBEntityState.Added);
        tIns.setFins_id(fins.getId().toString());
        tIns.setFlow_id(fins.getFlow_id());
        tIns.setTask_id(taskEntity.getId().toString());
        tIns.setTask_name(taskEntity.getTask_name());
        tIns.setStart_time(LocalDateTime.now());
        if (taskEntity.getTimeout() != null && taskEntity.getTimeout() > 0) {
            tIns.setDead_line(LocalDateTime.now().plusHours(taskEntity.getTimeout()));
        }
        tIns.setProcess_status(EProcessStatus.UnProcess.getStatus());
        if (preTaskEntity != null) {
            tIns.setPre_task_id(preTaskEntity.getId().toString());
        }
        if (processed) {
            tIns.setProcess_time(LocalDateTime.now());
            tIns.setProcess_status(EProcessStatus.Processed.getStatus());
        }
        collection.getData().add(tIns);
        dataAccess.update(collection);
        return tIns;
    }

    public WfTInsEntity getTinsById(String tInsId) {
        if (StringUtils.isEmpty(tInsId)) {
            return null;
        }
        Long longId = Long.parseLong(tInsId);
        List<WfTInsEntity> list = dataAccess.queryById(WfTInsEntity.TABLE_CODE, longId).getGenericData(WfTInsEntity.class);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
