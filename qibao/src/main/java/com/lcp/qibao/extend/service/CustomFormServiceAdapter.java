package com.lcp.qibao.extend.service;

import com.lcp.qibao.extend.IFormTrigger;
import com.lcp.qibao.extend.LoadSubTableDataService;
import com.lcp.qibao.extend.LookupColumnDataService;

import java.util.List;

/**
 * 表单扩展服务适配器
 */
public interface CustomFormServiceAdapter {

    List<IFormTrigger> getAllFormTrigger();

    IFormTrigger getFormTriggerByPath(String path);

    /**
     * 获取表单子表扩展服务
     *
     * @return
     */
    List<LoadSubTableDataService> getSubTableDataServiceList();

    /**
     * 返回表下可用扩展服务
     *
     * @param tableId
     * @return
     */
    List<LoadSubTableDataService> getSubTableDataServiceByTableId(String tableId);

    /**
     * 根据服务id获取具体的服务实例
     *
     * @param tableId
     * @param serviceId
     * @return
     */
    LoadSubTableDataService getSubTableDataServiceById(String tableId, String serviceId);


    List<LookupColumnDataService> getLookupColumnDataServiceList();

    List<LookupColumnDataService> getLookupColumnDataServiceList(String columnId);

    LookupColumnDataService getLookupColumnDataService(String columnId, String serviceId);
}
