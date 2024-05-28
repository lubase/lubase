package com.lubase.core.extend.service;

import com.lubase.core.extend.IColumnRemoteService;

import java.util.List;
import java.util.Map;

public interface ColumnRemoteServiceAdapter {
    IColumnRemoteService getServiceByName(String serviceName);

    List<IColumnRemoteService> getAllService();

    Map<String, IColumnRemoteService> getAllServiceMap();
}
