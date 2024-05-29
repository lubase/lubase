package com.lubase.orm.extend.service;

import com.lubase.orm.extend.IColumnRemoteService;

import java.util.List;
import java.util.Map;

public interface ColumnRemoteServiceAdapter {
    IColumnRemoteService getServiceByName(String serviceName);

    List<IColumnRemoteService> getAllService();

    Map<String, IColumnRemoteService> getAllServiceMap();
}
