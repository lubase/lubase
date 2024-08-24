package com.lubase.core.service;

import com.lubase.core.util.ClientMacro;
import com.lubase.orm.model.DbCollection;

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

    /**
     * 不分页获取全部明细数据
     *
     * @param pageId         页面id
     * @param clientMacro    客户端宏变量
     * @param searchParam    搜索区域条件
     * @param queryParam     分页参数
     * @param fullTextSearch 模糊搜索参数
     * @return
     */
    DbCollection getGridAllDataByPageId(String pageId, ClientMacro clientMacro, String searchParam, String queryParam, String fullTextSearch);

    String getIdListByFullTextSearch(String pageId, String searchStr);

    /**
     * 标准统计获取详情数据
     *
     * @param pageId
     * @param searchParamStr
     * @param queryParamStr
     * @param clientMacro
     * @param rowValue
     * @param colValue
     * @return
     */
    DbCollection getStatisticsInfo(String pageId, String searchParamStr, String queryParamStr, ClientMacro clientMacro, String rowValue, String colValue);

    /**
     * 不分页获取全部明细数据
     *
     * @param pageId
     * @param searchParam
     * @param clientMacro
     * @param rowValue
     * @param colValue
     * @return
     */
    DbCollection getStatisticsInfoNoPaging(String pageId, String searchParam, ClientMacro clientMacro, String rowValue, String colValue);
}
