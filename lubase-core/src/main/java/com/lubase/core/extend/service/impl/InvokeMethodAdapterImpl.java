package com.lubase.core.extend.service.impl;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.core.extend.IGetLeftDataService;
import com.lubase.core.extend.IGetMainDataService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.extend.service.InvokeMethodAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvokeMethodAdapterImpl implements ExtendAppLoadCompleteService, InvokeMethodAdapter {
    /**
     * method方法
     */
    List<IInvokeMethod> invokeMethodList;
    /**
     * 左树数据源
     */
    List<IGetLeftDataService> getLeftDataServiceList;
    /**
     * 列表数据源
     */
    List<IGetMainDataService> getMainDataServiceList;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (invokeMethodList == null) {
            invokeMethodList=new ArrayList<>();
            getLeftDataServiceList=new ArrayList<>();
            getMainDataServiceList=new ArrayList<>();
            for (IInvokeMethod invokeMethod : applicationContext.getBeansOfType(IInvokeMethod.class).values()) {
                if (invokeMethodList.stream().noneMatch(m -> invokeMethod.getId().equals(m.getId()))) {
                    invokeMethodList.add(invokeMethod);
                }
            }
            for (IGetLeftDataService getLeftDataService : applicationContext.getBeansOfType(IGetLeftDataService.class).values()) {
                if (getLeftDataServiceList.stream().noneMatch(m -> getLeftDataService.getId().equals(m.getId()))) {
                    getLeftDataServiceList.add(getLeftDataService);
                }
            }
            for (IGetMainDataService getMainDataService : applicationContext.getBeansOfType(IGetMainDataService.class).values()) {
                if (getMainDataServiceList.stream().noneMatch(m -> getMainDataService.getId().equals(m.getId()))) {
                    getMainDataServiceList.add(getMainDataService);
                }
            }
        }
    }

    @Override
    public void clearData() {
        invokeMethodList = null;
        getLeftDataServiceList = null;
        getMainDataServiceList = null;
    }

    @Override
    public IGetLeftDataService getLeftDataService(Long methodId) {
        if (methodId == null) {
            return null;
        }
        String id = methodId.toString();
        return getLeftDataServiceList.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
    }

    @Override
    public IGetMainDataService getMainDataService(Long methodId) {
        if (methodId == null) {
            return null;
        }
        String id = methodId.toString();
        return getMainDataServiceList.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
    }

    @Override
    public IInvokeMethod getInvokeMethodById(Long methodId) {
        if (methodId == null) {
            return null;
        }
        String id = methodId.toString();
        return invokeMethodList.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
    }

    @Override
    public List<IGetLeftDataService> getLeftDataServiceList() {
        return getLeftDataServiceList;
    }

    @Override
    public List<IGetMainDataService> getMainDataServiceList() {
        return getMainDataServiceList;
    }

    @Override
    public List<IInvokeMethod> getInvokeMethodByIdList() {
        return invokeMethodList;
    }
}
