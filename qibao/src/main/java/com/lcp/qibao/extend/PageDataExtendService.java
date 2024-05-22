package com.lcp.qibao.extend;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.util.ClientMacro;

/**
 * 页面数据扩展服务
 */
public interface PageDataExtendService {
    /**
     * 扩展页面的id
     *
     * @return
     */
    String getPageId();

    /**
     * 扩展功能描述
     *
     * @return
     */
    String getDescription();

    /**
     * 如果设置为true，则所有页面生效。已过期
     *
     * @return
     */
    @Deprecated
    default Boolean allPageValid() {
        return false;
    }

    /**
     * 列表页面查询前事件。可动态修改查询条件
     *
     * @param pageEntity
     * @param queryOption
     */
    default void beforeExecuteQuery(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {
    }

    /**
     * 列表页面数据查询完毕返回客户端之前事件
     *
     * @param collection
     * @return
     */
    default DbCollection beforeReturnMainData(String pageId, DbCollection collection) {
        return collection;
    }
}
