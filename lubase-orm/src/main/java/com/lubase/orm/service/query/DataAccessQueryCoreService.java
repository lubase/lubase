package com.lubase.orm.service.query;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;

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

    /**
     * 数据统计
     *
     * @param queryOption
     * @return
     */
    int queryCount(QueryOption queryOption);

}
