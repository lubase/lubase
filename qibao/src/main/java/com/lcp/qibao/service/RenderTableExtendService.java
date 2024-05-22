package com.lcp.qibao.service;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.auto.entity.SsPageEntity;
import com.lcp.qibao.extend.PageDataExtendService;
import com.lcp.qibao.model.PageInfoVO;
import com.lcp.qibao.util.ClientMacro;

import java.util.List;

/**
 * 主列表查询扩展服务
 */
public interface RenderTableExtendService {
    /**
     * 根据页面id获取扩展功能
     *
     * @param pageId
     * @return
     */
    List<PageDataExtendService> getCurrentExtendService(String pageId);
    /**
     * 执行页面模板的扩展方法。执行顺序1
     *
     * @param pageEntity
     * @param queryOption
     */
    void executePageTemplateExtend(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro);

    /**
     * 页面列表数据加载前 执行顺序2
     *
     * @param pageEntity
     * @param extendServiceList
     * @param queryOption
     */
    void beforeExecuteQueryEvent(DbEntity pageEntity, List<PageDataExtendService> extendServiceList, QueryOption queryOption, ClientMacro clientMacro);

    /**
     * 页面模板执行返回后 执行顺序3
     *
     * @param pageEntity
     * @param collection
     * @param clientMacro
     */
    void beforeReturnPageTemplateExtend(DbEntity pageEntity, DbCollection collection, ClientMacro clientMacro);

    /**
     * 页面返回前事件 执行顺序4
     *
     * @param pageId
     * @param extendServiceList
     * @param collection
     */
    void beforeReturnMainDataEvent(String pageId, List<PageDataExtendService> extendServiceList, DbCollection collection);

    PageInfoVO beforeReturnPageInfo(SsPageEntity pageEntity, PageInfoVO pageInfoVO);
}
