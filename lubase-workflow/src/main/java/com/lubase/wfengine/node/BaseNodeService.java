package com.lubase.wfengine.node;

import com.lubase.core.model.DbCollection;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfTEventDao;
import com.lubase.wfengine.model.ETaskType;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import com.lubase.wfengine.service.WfTEventService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseNodeService {

    @Autowired
    WfTEventDao wfTEventDao;

    @Autowired
    WfTEventService eventService;

    @Autowired
    SendEventToRemoteService sendEventToRemoteService;

    /**
     * 获取当前节点类型
     *
     * @return
     */
    public abstract ETaskType getTaskType();

    /**
     * 执行具体任务节点的内容
     *
     * @return
     */
    public abstract Boolean runTask(WfFInsEntity fIns, WfTaskEntity taskEntity, WfTInsEntity tIns, WfTEventEntity event, DbCollection bisData);

    /**
     * 创建待处理事件
     *
     * @param fIns
     * @param tIns
     */
    public void createEventJob(WfFInsEntity fIns, WfTInsEntity tIns) {
        WfTEventEntity entity = wfTEventDao.createWfTEvent(fIns, tIns);
        if (entity != null) {
            sendEventToRemoteService.sendWorkFlowEngineEvent(fIns.getService_id(), fIns.getData_id(), entity);
        }
    }
}
