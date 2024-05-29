package com.lubase.core.extend.service.impl;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.core.extend.GroupPageExtendService;
import com.lubase.core.extend.PageDataExtendService;
import com.lubase.core.extend.PageInfoExtendService;
import com.lubase.core.extend.PageTemplateExtendService;
import com.lubase.core.extend.service.PageServiceAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageServiceAdapterImpl implements ExtendAppLoadCompleteService, PageServiceAdapter {
    /**
     * 页面扩展服务
     */
    List<PageDataExtendService> pageDataExtendServiceList;

    /**
     * 页面模板扩展服务
     */
    List<PageTemplateExtendService> pageTemplateExtendServiceList;
    /**
     * 页面配置信息扩展服务
     */
    List<PageInfoExtendService> pageInfoExtendServiceList;
    /**
     * 页签页面获取页签服务
     */
    List<GroupPageExtendService> groupPageExtendServiceList;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (pageDataExtendServiceList == null) {
            pageDataExtendServiceList = new ArrayList<>(applicationContext.getBeansOfType(PageDataExtendService.class).values());
        }
        if (pageTemplateExtendServiceList == null) {
            pageTemplateExtendServiceList = new ArrayList<>(applicationContext.getBeansOfType(PageTemplateExtendService.class).values());
        }
        if (pageInfoExtendServiceList == null) {
            pageInfoExtendServiceList = new ArrayList<>(applicationContext.getBeansOfType(PageInfoExtendService.class).values());
        }
        if (groupPageExtendServiceList == null) {
            groupPageExtendServiceList = new ArrayList<>(applicationContext.getBeansOfType(GroupPageExtendService.class).values());
        }
    }

    @Override
    public void clearData() {
        pageDataExtendServiceList = null;
        pageTemplateExtendServiceList = null;
        pageInfoExtendServiceList = null;
        groupPageExtendServiceList = null;
    }

    @Override
    public List<PageDataExtendService> getPageDataExtendServiceList() {
        return pageDataExtendServiceList;
    }

    @Override
    public List<PageTemplateExtendService> getPageTemplateExtendServiceList() {
        return pageTemplateExtendServiceList;
    }

    @Override
    public List<PageInfoExtendService> getPageInfoExtendServiceList() {
        return pageInfoExtendServiceList;
    }

    @Override
    public List<GroupPageExtendService> getGroupPageExtendServiceList() {
        return groupPageExtendServiceList;
    }

    @Override
    public GroupPageExtendService getGroupPageExtendService(String pageId) {
        if (groupPageExtendServiceList == null || groupPageExtendServiceList.size() == 0) {
            return null;
        }
        for (GroupPageExtendService extendService : groupPageExtendServiceList) {
            if (extendService.getPageId().contains(pageId)) {
                return extendService;
            }
        }
        return null;
    }
}
