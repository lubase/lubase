package com.lubase.core.service.impl;

import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.extend.PageDataExtendService;
import com.lubase.core.extend.PageInfoExtendService;
import com.lubase.core.extend.PageTemplateExtendService;
import com.lubase.core.extend.service.PageServiceAdapter;
import com.lubase.core.model.PageInfoVO;
import com.lubase.core.service.RenderTableExtendService;
import com.lubase.core.util.ClientMacro;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RenderTableExtendServiceImpl implements RenderTableExtendService {

    @Autowired
    PageServiceAdapter pageServiceAdapter;

    @Autowired
    DataAccess dataAccess;

    @Override
    public List<PageDataExtendService> getCurrentExtendService(String pageId) {
        List<PageDataExtendService> pageDataExtendServiceList = pageServiceAdapter.getPageDataExtendServiceList();
        if (pageDataExtendServiceList == null || pageDataExtendServiceList.size() == 0) {
            return new ArrayList<>();
        }
        return pageServiceAdapter.getPageDataExtendServiceList().stream().filter(s -> s.getPageId().contains(pageId)
                || s.allPageValid()).collect(Collectors.toList());
    }


    @Override
    public void beforeExecuteQueryEvent(DbEntity pageEntity, List<PageDataExtendService> extendServiceList, QueryOption queryOption, ClientMacro clientMacro) {
        if (extendServiceList == null) {
            return;
        }
        for (PageDataExtendService extendService : extendServiceList) {
            if (extendService != null) {
                extendService.beforeExecuteQuery(pageEntity, queryOption, clientMacro);
            }
        }
    }

    @Override
    public DbCollection executePageTemplate(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {
        PageTemplateExtendService pageTemplateExtendService = pageTemplateExtendService(pageEntity.get(SsPageEntity.COL_MASTER_PAGE).toString());
        if (pageTemplateExtendService == null) {
            return dataAccess.query(queryOption);
        } else {
            return pageTemplateExtendService.executePageTemplate(dataAccess, pageEntity, queryOption, clientMacro);
        }
    }

    @Override
    public void executePageTemplateExtend(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {
        PageTemplateExtendService pageTemplateExtendService = pageTemplateExtendService(pageEntity.get(SsPageEntity.COL_MASTER_PAGE).toString());
        if (pageTemplateExtendService == null) {
            return;
        }
        pageTemplateExtendService.beforeExecuteQuery(pageEntity, queryOption, clientMacro);
    }

    PageTemplateExtendService pageTemplateExtendService(String pageTemplateCode) {
        List<PageTemplateExtendService> pageTemplateExtendServiceList = pageServiceAdapter.getPageTemplateExtendServiceList();
        if (pageTemplateExtendServiceList == null || pageTemplateExtendServiceList.size() == 0) {
            return null;
        }
        return pageTemplateExtendServiceList.stream().filter(s -> s.getTemplateCode().equals(pageTemplateCode)).findFirst().orElse(null);
    }

    @Override
    public void beforeReturnMainDataEvent(String pageId, List<PageDataExtendService> extendServiceList, DbCollection collection) {
        if (extendServiceList == null) {
            return;
        }
        for (PageDataExtendService extendService : extendServiceList) {
            if (extendService != null) {
                extendService.beforeReturnMainData(pageId, collection);
            }
        }
    }

    @Override
    public PageInfoVO beforeReturnPageInfo(SsPageEntity pageEntity, PageInfoVO pageInfoVO) {
        List<PageInfoExtendService> pageInfoExtendServiceList = pageServiceAdapter.getPageInfoExtendServiceList();
        if (pageInfoExtendServiceList == null || pageInfoExtendServiceList.size() == 0 || pageEntity == null) {
            return pageInfoVO;
        }
        String pageId = pageEntity.getId().toString();
        for (PageInfoExtendService extendService : pageInfoExtendServiceList) {
            if (!extendService.getPageId().contains(pageId)) {
                continue;
            }
            pageInfoVO = extendService.beforeReturnPageInfo(pageEntity, pageInfoVO);
        }
        return pageInfoVO;
    }

    @Override
    public void beforeReturnPageTemplateExtend(DbEntity pageEntity, DbCollection collection, ClientMacro clientMacro) {
        PageTemplateExtendService pageTemplateExtendService = pageTemplateExtendService(pageEntity.get(SsPageEntity.COL_MASTER_PAGE).toString());
        if (pageTemplateExtendService == null) {
            return;
        }
        pageTemplateExtendService.beforeReturnMainData(pageEntity, collection, clientMacro);
    }
}
