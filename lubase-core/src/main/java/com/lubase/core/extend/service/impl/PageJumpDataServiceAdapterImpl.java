package com.lubase.core.extend.service.impl;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.core.extend.PageJumpDataService;
import com.lubase.core.extend.service.PageJumpDataServiceAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageJumpDataServiceAdapterImpl implements PageJumpDataServiceAdapter, ExtendAppLoadCompleteService {
    @Override
    public PageJumpDataService getPageJumpDataService(String methodId) {
        if (this.pageJumpDataServiceList == null) {
            return null;
        }
        for (PageJumpDataService service : pageJumpDataServiceList) {
            if (methodId.equals(service.getId())) {
                return service;
            }
        }
        return null;
    }

    List<PageJumpDataService> pageJumpDataServiceList;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (pageJumpDataServiceList == null) {
            pageJumpDataServiceList = new ArrayList<>(applicationContext.getBeansOfType(PageJumpDataService.class).values());
        }
    }

    @Override
    public void clearData() {
        this.pageJumpDataServiceList = null;
    }
}
