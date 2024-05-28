package com.lubase.wfengine.remote;


import com.lubase.core.model.DbCollection;
import com.lubase.starter.extend.WorkFlowExtendForService;

import java.util.List;

/**
 * 流程流转时获取远端数据，用于流程流转
 */
public interface RemoteBisDataService {

    /**
     * 获取实现的"获取流程主数据"类
     *
     * @return
     */
    List<WorkFlowExtendForService> bisDataForWorkFlowList();

    /**
     * 获取流程主数据
     *
     * @param serviceId 业务场景id
     * @param dataId    业务主数据id
     * @return
     */
    DbCollection getBisData(String serviceId, String dataId);
}
