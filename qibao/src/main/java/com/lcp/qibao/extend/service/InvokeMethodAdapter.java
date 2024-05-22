package com.lcp.qibao.extend.service;

import com.lcp.qibao.extend.IGetLeftDataService;
import com.lcp.qibao.extend.IGetMainDataService;
import com.lcp.qibao.extend.IInvokeMethod;

import java.util.List;

/**
 * method方法扩展适配器
 */
public interface InvokeMethodAdapter {
    IGetLeftDataService getLeftDataService(Long methodId);

    IGetMainDataService getMainDataService(Long methodId);

    IInvokeMethod getInvokeMethodById(Long methodId);

    List<IGetLeftDataService> getLeftDataServiceList();

    List<IGetMainDataService> getMainDataServiceList();

    List<IInvokeMethod> getInvokeMethodByIdList();
}
