package com.lcp.qibao.extend.service.impl;

import com.lcp.core.extend.ExtendAppLoadCompleteService;
import com.lcp.qibao.extend.PageJumpDataService;
import com.lcp.qibao.extend.service.PageJumpDataServiceAdapter;
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
