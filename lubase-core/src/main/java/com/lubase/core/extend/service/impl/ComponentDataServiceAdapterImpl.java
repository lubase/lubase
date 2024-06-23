package com.lubase.core.extend.service.impl;

import com.lubase.core.extend.UserCreateExtendService;
import com.lubase.core.extend.UserLoginExtendService;
import com.lubase.core.extend.UserSelectForComponentDataService;
import com.lubase.core.extend.service.UserInfoExtendServiceAdapter;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComponentDataServiceAdapterImpl implements UserInfoExtendServiceAdapter, ExtendAppLoadCompleteService {

    List<UserSelectForComponentDataService> componentServiceList;
    List<UserLoginExtendService> userLoginServiceList;
    List<UserCreateExtendService> userCreateServiceList;

    @Override
    public UserSelectForComponentDataService getComponentDataForSelectUserService() {
        if (componentServiceList != null && !componentServiceList.isEmpty()) {
            return componentServiceList.get(0);
        }
        return null;
    }

    @Override
    public UserLoginExtendService getUserLoginExtendService() {
        if (userLoginServiceList != null && !userLoginServiceList.isEmpty()) {
            return userLoginServiceList.get(0);
        }
        return null;
    }

    @Override
    public UserCreateExtendService getUserCreateExtendService() {
        if (userCreateServiceList != null && !userCreateServiceList.isEmpty()) {
            return userCreateServiceList.get(0);
        }
        return null;
    }

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (componentServiceList == null) {
            componentServiceList = new ArrayList<>(applicationContext.getBeansOfType(UserSelectForComponentDataService.class).values());
        }
        if (userLoginServiceList == null) {
            userLoginServiceList = new ArrayList<>(applicationContext.getBeansOfType(UserLoginExtendService.class).values());
        }
        if (userCreateServiceList == null) {
            userCreateServiceList = new ArrayList<>(applicationContext.getBeansOfType(UserCreateExtendService.class).values());
        }
    }

    @Override
    public void clearData() {
        componentServiceList = null;
        userLoginServiceList = null;
        userCreateServiceList = null;
    }
}
