package com.lubase.core.service;

import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.extend.PageDataExtendService;
import com.lubase.core.model.PageInfoVO;
import com.lubase.core.util.ClientMacro;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;

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
