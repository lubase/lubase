package com.lubase.orm.extend.service.impl;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.extend.service.ColumnRemoteServiceAdapter;
import com.lubase.orm.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ColumnRemoteServiceAdapterImpl implements ExtendAppLoadCompleteService, ColumnRemoteServiceAdapter {

    List<IColumnRemoteService> columnRemoteServiceList;

    Map<String, IColumnRemoteService> remoteServiceMap;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (columnRemoteServiceList == null) {
            remoteServiceMap = SpringUtil.getApplicationContext().getBeansOfType(IColumnRemoteService.class);
            columnRemoteServiceList = new ArrayList<>(remoteServiceMap.values());
        }
    }

    @Override
    public void clearData() {
        columnRemoteServiceList = new ArrayList<>();
        remoteServiceMap = new HashMap<>();
    }

    @Override
    public IColumnRemoteService getServiceByName(String serviceName) {
        if (remoteServiceMap == null) {
            return null;
        }
        IColumnRemoteService service = null;
        for (String key : remoteServiceMap.keySet()) {
            if (key.equals(serviceName)) {
                service = remoteServiceMap.get(key);
            }
        }
        return service;
    }

    @Override
    public List<IColumnRemoteService> getAllService() {
        return columnRemoteServiceList;
    }

    @Override
    public Map<String, IColumnRemoteService> getAllServiceMap() {
        return remoteServiceMap;
    }

}
