package com.lubase.core.extend.service;

import com.lubase.core.extend.IGetLeftDataService;
import com.lubase.core.extend.IGetMainDataService;
import com.lubase.core.extend.IInvokeMethod;

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
