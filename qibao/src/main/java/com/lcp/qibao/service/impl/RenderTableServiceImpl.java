package com.lcp.qibao.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.statistics.StatisticsOption;
import com.lcp.core.operate.EOperateMode;
import com.lcp.core.service.AppHolderService;
import com.lcp.core.service.DataAccess;
import com.lcp.core.service.query.StatisticsCoreService;
import com.lcp.core.util.ServerMacroService;
import com.lcp.core.util.TableFilterWrapper;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
import com.lcp.qibao.auto.entity.SsPageEntity;
import com.lcp.qibao.extend.IGetMainDataService;
import com.lcp.qibao.extend.PageDataExtendService;
import com.lcp.qibao.extend.service.InvokeMethodAdapter;
import com.lcp.qibao.model.DisplayListVO;
import com.lcp.qibao.model.ESearchConditionType;
import com.lcp.qibao.model.QueryParamDTO;
import com.lcp.qibao.model.SearchCondition;
import com.lcp.qibao.service.*;
import com.lcp.qibao.util.ClientMacro;
import com.lcp.qibao.util.InvokeDataSourceService;
import com.lcp.qibao.util.SearchCondition2TableFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RenderTableServiceImpl implements RenderTableService, RenderBaseService {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    InvokeMethodAdapter invokeMethodAdapter;

    @Autowired
    SearchCondition2TableFilterService searchCondition2TableFilterService;

    @Autowired
    InvokeDataSourceService sqlDataSource;

    @Autowired
    ServerMacroService serverMacroService;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    PersonalizationDataService personalizationDataService;

    @Autowired
    StatisticsCoreService statisticsCoreService;
    @Autowired
    RenderTableExtendService gridDataExtendService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AppFuncDataService appFuncDataService;
    String urlTemplate;

    public RenderTableServiceImpl(Environment environment) {
        this.urlTemplate = String.format("%s/fulltext", environment.getProperty("custom.fulltext-server"));
    }

    @Override
    public ServerMacroService getServerMacroService() {
        return serverMacroService;
    }

    @Override
    public DbCollection getGridDataByPageId(String pageId, ClientMacro clientMacro, String searchParamStr, String queryParamsStr, String fullTextSearch) {
        if (StringUtils.isEmpty(pageId)) {
            return null;
        }
        SsPageEntity pageEntity = appFuncDataService.getPageById(pageId);
        //todo:此处需要验证是否有页面权限
        if (pageEntity == null || StringUtils.isEmpty(pageEntity.getGrid_query())) {
            return null;
        }
        List<PageDataExtendService> extendServices = gridDataExtendService.getCurrentExtendService(pageId);
        QueryOption serverQuery = JSON.parseObject(pageEntity.getGrid_query(), QueryOption.class);
        QueryParamDTO clientQuery = JSON.parseObject(queryParamsStr, QueryParamDTO.class);
        StatisticsOption statisticsOption = JSON.parseObject(pageEntity.getStatistics_setting(), StatisticsOption.class);
        DbCollection coll = null;
        if (serverQuery.getQueryType() == 2) {
            //注意: 只能接收无参数的方法。如果需要客户端传递的参数，请使用queryType=3。详见↓
            coll = sqlDataSource.queryDBCollectionBySql(Long.parseLong(pageId), serverQuery.getServerId());
        } else if (serverQuery.getQueryType() == 3) {
            IGetMainDataService service = invokeMethodAdapter.getMainDataService(serverQuery.getServerId());
            coll = service.exe(pageId, clientMacro);
        } else {
            //todo 这里是否适合*
            if (StringUtils.isEmpty(serverQuery.getFixField())) {
                serverQuery.setFixField("*");
            }
            if (StringUtils.isEmpty(fullTextSearch)) {
                replaceClientMacro(serverQuery.getTableFilter(), clientMacro);
                if (!StringUtils.isEmpty(searchParamStr)) {
                    List<SearchCondition> list = JSON.parseArray(searchParamStr, SearchCondition.class);
                    TableFilter searchFilter = searchCondition2TableFilterService.convertToTableFilter(list);
                    if (searchFilter != null) {
                        serverQuery.getTableFilter().getChildFilters().add(searchFilter);
                    }
                }
                mergeClientQuery(serverQuery, clientQuery);
            } else {
                //增加全文检索的支持
                String ids = "0";
                //从远端获取全文检索的id
                try {
                    String tmpIds = getIdListByFullTextSearch(pageId, fullTextSearch);
                    if (tmpIds != null) {
                        ids = tmpIds;
                    }
                } catch (Exception ex) {
                    log.error("全文检索查询失败,pageId" + pageId, ex);
                }
                if (clientQuery != null) {
                    //替换query中的查询参数
                    serverQuery.setPageIndex(clientQuery.getPageIndex());
                    serverQuery.setPageSize(clientQuery.getPageSize());
                }
                serverQuery.getTableFilter().getChildFilters().add(new TableFilter("id", ids, EOperateMode.In));
            }
            if (statisticsOption != null && !StringUtils.isEmpty(statisticsOption.getRowField()) && !StringUtils.isEmpty(statisticsOption.getColumnField())) {
                coll = statisticsCoreService.queryStatistics(serverQuery, statisticsOption);
            } else {
                serverQuery.setEnableColAccessControl(true);
                gridDataExtendService.executePageTemplateExtend(pageEntity, serverQuery, clientMacro);
                gridDataExtendService.beforeExecuteQueryEvent(pageEntity, extendServices, serverQuery, clientMacro);
                coll = dataAccess.query(serverQuery);
            }
        }
        int i = 1;
        for (DbField f : coll.getTableInfo().getFieldList()) {
            f.setOrderId(i);
            i++;
        }
        //获取个性化配置。如果后续除了字段设定还增加别的设置信息，可设置VO对象，序列化后存储到此字段，共用 collection对象的personalization属性
        DisplayListVO displayListVO = personalizationDataService.getDisplaySetting(Long.parseLong(pageId), appHolderService.getUser().getId());
        String displayColumns = displayListVO.getColumnIds();
//        if (!StringUtils.isEmpty(displayColumns)) {
//            List<String> validList = new ArrayList<>();
//            List<DbField> allFieldList = coll.getTableInfo().getFieldList();
//            for (String columnId : displayColumns.split(",")) {
//                if (allFieldList.stream().anyMatch(d -> d.getId().equals(columnId))) {
//                    validList.add(columnId);
//                }
//            }
//            displayListVO.setColumnIds(String.join(",", validList));
//        }
        coll.setPersonalization(JSON.toJSONString(displayListVO));
        gridDataExtendService.beforeReturnPageTemplateExtend(pageEntity, coll, clientMacro);
        gridDataExtendService.beforeReturnMainDataEvent(pageId, extendServices, coll);
        return coll;
    }

    @Override
    public String getIdListByFullTextSearch(String pageId, String searchStr) {
        String url = String.format("%s/getIdsByStr?pageId=" + pageId + "&searchStr=" + searchStr, urlTemplate);
        String ids = restTemplate.getForEntity(url, String.class).getBody();
        return ids;
    }


    private void mergeClientQuery(QueryOption serverQuery, QueryParamDTO clientQuery) {
        if (clientQuery != null) {
            //替换query中的查询参数
            serverQuery.setPageIndex(clientQuery.getPageIndex());
            serverQuery.setPageSize(clientQuery.getPageSize());
            String clientSort = processClientSort(clientQuery.getSortField());
            //如果存在客户端排序条件，则客户端排序条件优先级高
            if (!StringUtils.isEmpty(clientSort)) {
                if (!StringUtils.isEmpty(serverQuery.getSortField())) {
                    serverQuery.setSortField(String.format("%s,%s", clientSort, serverQuery.getSortField()));
                } else {
                    serverQuery.setSortField(clientSort);
                }
            }
            serverQuery.setSortField(processSortField(serverQuery.getSortField()));
        }
    }


    private String processClientSort(String clientSortField) {
        if (StringUtils.isEmpty(clientSortField)) {
            return "";
        }
        String clientSort = "";
        //目前仅支持单列排序
        if (!clientSortField.contains(" ") || clientSortField.split(" ").length != 2 || clientSortField.contains(",")) {
            clientSort = "";
        } else {
            clientSort = clientSortField.toLowerCase().replace("#", ".");
        }
        return clientSort;
    }

    private String processSortField(String sortField) {
        //空或者只有1个排序字段则不处理
        if (StringUtils.isEmpty(sortField) || !sortField.contains(",")) {
            return sortField;
        }
        List<String> sortColumns = new ArrayList<>();
        List<String> allowSortField = new ArrayList<>();
        for (String sort : sortField.split(",")) {
            String columnCode = sort.split(" ")[0];
            if (!sortColumns.contains(columnCode)) {
                sortColumns.add(columnCode);
                allowSortField.add(sort);
            }
        }
        return String.join(",", allowSortField);
    }


    @Override
    public DbCollection getStatisticsInfo(String pageId, String queryParamsStr, ClientMacro clientMacro, String
            rowValue, String colValue) {
        return getStatisticsInfo(pageId, queryParamsStr, clientMacro, rowValue, colValue, false);
    }

    @Override
    public DbCollection getStatisticsInfoNoPaging(String pageId, String queryParamsStr, ClientMacro
            clientMacro, String rowValue, String colValue) {
        return getStatisticsInfo(pageId, queryParamsStr, clientMacro, rowValue, colValue, true);
    }

    private DbCollection getStatisticsInfo(String pageId, String queryParamsStr, ClientMacro clientMacro, String
            rowValue, String colValue, Boolean allData) {
        if (StringUtils.isEmpty(pageId)) {
            return null;
        }
        SsPageEntity pageEntity = appFuncDataService.getPageById(pageId);
        //todo:此处需要验证是否有页面权限
        if (pageEntity == null || StringUtils.isEmpty(pageEntity.getGrid_query())) {
            return null;
        }
        QueryOption serverQuery = JSON.parseObject(pageEntity.getGrid_query(), QueryOption.class);
        QueryParamDTO clientQuery = JSON.parseObject(queryParamsStr, QueryParamDTO.class);
        StatisticsOption statisticsOption = JSON.parseObject(pageEntity.getStatistics_setting(), StatisticsOption.class);
        DbCollection coll = null;
        //mergeFilterQuery(serverQuery, clientQuery, statisticsOption, rowValue, colValue);
        mergeClientQuery(serverQuery, clientQuery);
        if (StringUtils.isEmpty(serverQuery.getFixField())) {
            serverQuery.setFixField("*");
        }
        replaceClientMacro(serverQuery.getTableFilter(), clientMacro);
        serverQuery.setEnableColAccessControl(true);
        if (allData) {
            coll = dataAccess.queryAllData(serverQuery);
        } else {
            coll = dataAccess.query(serverQuery);
        }
        int i = 1;
        for (DbField f : coll.getTableInfo().getFieldList()) {
            f.setOrderId(i);
            i++;
        }
        return coll;
    }

    private void mergeFilterQuery(QueryOption serverQuery, QueryOption clientQuery, StatisticsOption
            statisticsOption, String rowValue, String colValue) {
        if (clientQuery == null) {
            clientQuery = new QueryOption();
        }
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        DbTable table = dataAccess.initTableInfoByTableCode(serverQuery.getTableName());
        if (!StringUtils.isEmpty(rowValue)) {
            DbField rowField = table.getFieldList().stream().filter(f -> f.getCode().equals(statisticsOption.getRowField())).findFirst().orElse(null);
            filterWrapper.eq(rowField.getCode(), rowValue);
        }
        if (!StringUtils.isEmpty(colValue)) {
            DbField columnField = table.getFieldList().stream().filter(f -> f.getCode().equals(statisticsOption.getColumnField())).findFirst().orElse(null);
            filterWrapper.eq(columnField.getCode(), colValue);
        }
        if (clientQuery.getTableFilter() != null) {
            filterWrapper.addFilter(clientQuery.getTableFilter());
        }
        clientQuery.setTableFilter(filterWrapper.build());
    }


}
