package com.lubase.core.extend.service.impl;

import com.lubase.core.extend.ComponentDataForSelectUserService;
import com.lubase.core.extend.service.ComponentDataServiceAdapter;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComponentDataServiceAdapterImpl implements ComponentDataServiceAdapter, ExtendAppLoadCompleteService {

    List<ComponentDataForSelectUserService> componentServiceList;

    @Override
    public ComponentDataForSelectUserService getComponentDataForSelectUserService() {
        if (componentServiceList != null && componentServiceList.size() > 0 && componentServiceList.get(0).enableSelectUserList()) {
            return componentServiceList.get(0);
        }
        return null;
    }

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (componentServiceList == null) {
            componentServiceList = new ArrayList<>(applicationContext.getBeansOfType(ComponentDataForSelectUserService.class).values());
        }
    }

    @Override
    public void clearData() {
        componentServiceList = null;
    }
}
