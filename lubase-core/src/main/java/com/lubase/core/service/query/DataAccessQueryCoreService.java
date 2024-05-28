package com.lubase.core.service.query;

import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;

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
