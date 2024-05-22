package com.lcp.qibao.extend;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.util.ClientMacro;

/**
 * 页面模板服务
 */
public interface PageTemplateExtendService {
    /**
     * 解析数据
     *
     * @param pageEntity
     * @param queryOption
     * @param clientMacro
     */
    void beforeExecuteQuery(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro);

    /**
     * 返回主列表数据前事件
     * @param pageEntity
     * @param collection
     * @param clientMacro
     */
    void beforeReturnMainData(DbEntity pageEntity, DbCollection collection, ClientMacro clientMacro);
    /**
     * 设置页面模板代码
     *
     * @return
     */
    String getTemplateCode();
}
