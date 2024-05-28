package com.lubase.wfengine.dao;

import com.lubase.core.QueryOption;
import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.core.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.model.EEventStatus;
import com.lubase.wfengine.model.EProcessStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class WfTEventDao {
    @Autowired
    DataAccess dataAccess;

    /**
     * 创建流程事件
     *
     * @param fIns
     * @param tIns
     */
    public WfTEventEntity createWfTEvent(WfFInsEntity fIns, WfTInsEntity tIns) {
        DbCollection collection = dataAccess.getEmptyData(WfTEventEntity.TABLE_CODE);
        WfTEventEntity eventEntity = new WfTEventEntity();
        eventEntity.setState(EDBEntityState.Added);
        eventEntity.setFlow_id(fIns.getFlow_id());
        eventEntity.setFins_id(fIns.getId().toString());
        eventEntity.setTask_id(tIns.getTask_id());
        eventEntity.setTins_id(tIns.getId().toString());
        eventEntity.setStatus(EProcessStatus.UnProcess.getStatus());
        eventEntity.setCreate_time(LocalDateTime.now());
        collection.getData().add(eventEntity);
        if (dataAccess.update(collection) > 0) {
            return eventEntity;
        } else {
            return null;
        }
    }

    /**
     * 获取待处理的事件列表
     *
     * @param count
     * @return
     */
    public List<WfTEventEntity> getUnProcessEvent(Integer count) {
        QueryOption queryOption = new QueryOption(WfTEventEntity.TABLE_CODE);
        queryOption.setFixField("*");
        queryOption.setBuildLookupField(false);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(WfTEventEntity.COL_STATUS, EEventStatus.UnProcess.getStatus());
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setSortField(WfTEventEntity.COL_CREATE_TIME + " asc");
        return dataAccess.query(queryOption).getGenericData(WfTEventEntity.class);
    }

    @SneakyThrows
    public Integer updateEventStatus(Long id, EEventStatus status) {
        DbCollection collection = dataAccess.queryById(WfTEventEntity.TABLE_CODE, id);
        if (collection.getData().size() == 1) {
            DbEntity entity = collection.getData().get(0);
            entity.setState(EDBEntityState.Modified);
            entity.put(WfTEventEntity.COL_STATUS, status.getStatus());
            entity.put(WfTEventEntity.COL_PROCESS_TIME, LocalDateTime.now());
            return dataAccess.update(collection);
        } else {
            throw new InvokeCommonException(String.format("wf_tevent 事件不存在:%s不存在", id));
        }
    }
}
