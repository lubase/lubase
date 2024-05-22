package com.lcp.qibao.extend;

import com.lcp.core.model.DbCollection;

public interface WorkFlowExtendForService {
    /**
     * 业务场景id
     *
     * @return
     */
    String getServiceId();

    /**
     * 流业务长描述
     *
     * @return
     */
    String getDescription();

    /**
     * 获取业务数据
     *
     * @param serviceId 业务模板id
     * @param dataId    业务数据id
     * @return
     */
    DbCollection getData(String serviceId, String dataId);

    /**
     * 流程事件监听
     *
     * @param eventType
     * @param msg
     */
    default void registerEvent(String eventType, String msg) {
    }

}
