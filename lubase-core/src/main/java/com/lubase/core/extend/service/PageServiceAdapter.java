package com.lubase.core.extend.service;

import com.lubase.core.extend.GroupPageExtendService;
import com.lubase.core.extend.PageDataExtendService;
import com.lubase.core.extend.PageInfoExtendService;
import com.lubase.core.extend.PageTemplateExtendService;

import java.util.List;

/**
 * 页面扩展服务适配器
 */
public interface PageServiceAdapter {

    /**
     * 页面模板扩展服务
     */
    List<PageTemplateExtendService> getPageTemplateExtendServiceList();

    /**
     * 获取页面配置信息扩展服务
     *
     * @return
     */
    List<PageInfoExtendService> getPageInfoExtendServiceList();

    /**
     * 页签页面获取页签扩展服务
     *
     * @return
     */
    List<GroupPageExtendService> getGroupPageExtendServiceList();

    /**
     * 页面页签扩展服务
     *
     * @param pageId
     * @return
     */
    GroupPageExtendService getGroupPageExtendService(String pageId);

    /**
     * 页面列表扩展服务
     */
    List<PageDataExtendService> getPageDataExtendServiceList();
}
