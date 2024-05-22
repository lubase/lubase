package com.lcp.qibao.service;

import com.lcp.core.model.DbCollection;
import com.lcp.qibao.util.ClientMacro;

/**
 * 渲染列表服务
 */
public interface RenderTableService {
    /**
     * @param pageId         页面id
     * @param clientMacro    客户端宏变量
     * @param searchParam    搜索区域条件
     * @param queryParam     分页参数
     * @param fullTextSearch 模糊搜索参数
     * @return
     */
    DbCollection getGridDataByPageId(String pageId, ClientMacro clientMacro, String searchParam, String queryParam, String fullTextSearch);

    String getIdListByFullTextSearch(String pageId, String searchStr);

    /**
     * 标准统计获取详情数据
     *
     * @param pageId
     * @param queryParamsStr
     * @param clientMacro
     * @param rowValue
     * @param colValue
     * @return
     */
    DbCollection getStatisticsInfo(String pageId, String queryParamsStr, ClientMacro clientMacro, String rowValue, String colValue);

    /**
     * 不分页获取全部明细数据
     *
     * @param pageId
     * @param queryParamsStr
     * @param clientMacro
     * @param rowValue
     * @param colValue
     * @return
     */
    DbCollection getStatisticsInfoNoPaging(String pageId, String queryParamsStr, ClientMacro clientMacro, String rowValue, String colValue);
}
