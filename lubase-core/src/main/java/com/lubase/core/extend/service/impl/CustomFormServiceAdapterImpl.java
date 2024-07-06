package com.lubase.core.extend.service.impl;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.core.extend.IFormTrigger;
import com.lubase.core.extend.LoadSubTableDataService;
import com.lubase.core.extend.LookupColumnDataService;
import com.lubase.core.extend.service.CustomFormServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomFormServiceAdapterImpl implements CustomFormServiceAdapter, ExtendAppLoadCompleteService {

    List<IFormTrigger> formTriggerList;

    List<LoadSubTableDataService> subTableDataServiceList;

    List<LookupColumnDataService> lookupColumnDataServiceList;

    @Override
    public void clearData() {
        formTriggerList = null;
        subTableDataServiceList = null;
        lookupColumnDataServiceList = null;
    }

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (formTriggerList == null) {
            formTriggerList = new ArrayList<>(applicationContext.getBeansOfType(IFormTrigger.class).values());
        }
        if (subTableDataServiceList == null) {
            subTableDataServiceList = new ArrayList<>(applicationContext.getBeansOfType(LoadSubTableDataService.class).values());
        }
        if (lookupColumnDataServiceList == null) {
            lookupColumnDataServiceList = new ArrayList<>(applicationContext.getBeansOfType(LookupColumnDataService.class).values());
        }
    }

    @Override
    public List<IFormTrigger> getAllFormTrigger() {
        return formTriggerList;
    }

    @Override
    public IFormTrigger getFormTriggerByPath(String path) {
        return formTriggerList.stream().filter(t -> t.getClass().getName().equals(path)).findFirst().orElse(null);
    }

    @Override
    public List<LoadSubTableDataService> getSubTableDataServiceList() {
        return subTableDataServiceList;
    }

    @Override
    public List<LoadSubTableDataService> getSubTableDataServiceByTableId(String tableId) {
        if (subTableDataServiceList == null) {
            return new ArrayList<>();
        }
        List<LoadSubTableDataService> list = new ArrayList<>();
        for (LoadSubTableDataService service : subTableDataServiceList) {
            if (service.getParentTableId().equals(tableId)) {
                list.add(service);
            }
        }
        return list;
    }

    @Override
    public LoadSubTableDataService getSubTableDataServiceById(String tableId, String serviceId) {
        if (subTableDataServiceList == null) {
            return null;
        }
        for (LoadSubTableDataService service : subTableDataServiceList) {
            if (service.getParentTableId().equals(tableId) && service.getId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    @Override
    public List<LookupColumnDataService> getLookupColumnDataServiceList() {
        return lookupColumnDataServiceList;
    }

    @Override
    public List<LookupColumnDataService> getLookupColumnDataServiceList(String columnId) {
        List<LookupColumnDataService> serviceList = new ArrayList<>();
        for (LookupColumnDataService service : lookupColumnDataServiceList) {
            if (service.getColumnId().contains(columnId)) {
                serviceList.add(service);
            }
        }
        return serviceList;
    }

    @Override
    public LookupColumnDataService getLookupColumnDataService(String columnId) {
        for (LookupColumnDataService service : lookupColumnDataServiceList) {
            if (service.getColumnId().contains(columnId)) {
                return service;
            }
        }
        return null;
    }

}
