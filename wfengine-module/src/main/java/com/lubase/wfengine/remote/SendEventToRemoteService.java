package com.lubase.wfengine.remote;

import com.lubase.wfengine.auto.entity.WfCallbackEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.model.OpenEventModel;

public interface SendEventToRemoteService {
    /**
     * 发送流程引擎核心事件
     *
     * @param serviceId
     * @param dataId
     * @param entity
     */
    void sendWorkFlowEngineEvent(String serviceId, String dataId, WfTEventEntity entity);

    /**
     * 发送更新业务表事件到消息队列
     *
     * @param serviceId
     * @param dataId
     * @param entity
     */
    void sendUpdateBisTableEvent(String serviceId, String dataId, WfCallbackEntity entity);

    /**
     * 发送流程引擎公开事件
     *
     * @param model
     */
    void sendWorkFlowOpenEvent(OpenEventModel model);
}
