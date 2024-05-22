package com.lcp.core.service.query;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;

/**
 * DataAccess 核心查询服务
 */
public interface DataAccessQueryCoreService {
    /**
     * 数据查询
     *
     * @param queryOption
     * @return
     */
    DbCollection query(QueryOption queryOption, Boolean onlyQueryFieldList);

}
