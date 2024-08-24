package com.lubase.orm.mapper;

import com.lubase.orm.model.EDatabaseType;
import com.lubase.orm.model.QueryParamEntity;
import com.lubase.orm.model.SqlEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 拼接query生成的sql语句。需要支持多库才可以
 *
 * @author A
 */
@Slf4j
public class DataAccessSqlBuilder {
    public String queryList(QueryParamEntity queryParam) {
        String queryFields, joinCondition, where, sortFields;
        Integer pageIndex, pageSize, queryMode;
        queryFields = queryParam.getQueryFields();
        joinCondition = queryParam.getJoinCondition();
        where = queryParam.getWhere();
        sortFields = queryParam.getSortFields();
        pageIndex = queryParam.getPageIndex();
        pageSize = queryParam.getPageSize();
        queryMode = queryParam.getQueryMode();
        //5、拼接完整sql语句
        String sortStr = "";
        // queryMode  1:分页查询  2：全部查询
        if (queryMode == 1 && pageSize > 0 && pageIndex > 0) {
            //判断数据库类型
            if (queryParam.getDatabaseType().equals(EDatabaseType.Sqlserver.getType())) {
                if (StringUtils.isEmpty(sortFields)) {
                    sortFields = "(select 1)";
                }
                sortStr = String.format("order by %s offset %d rows fetch next %d rows only", sortFields, (pageIndex - 1) * pageSize, pageSize);
            } else if (queryParam.getDatabaseType().equals(EDatabaseType.Mysql.getType())) {
                if (StringUtils.isEmpty(sortFields)) {
                    sortStr = String.format("limit %s,%s", (pageIndex - 1) * pageSize, pageSize);
                } else {
                    sortStr = String.format("order by %s limit %s,%s", sortFields, (pageIndex - 1) * pageSize, pageSize);
                }
            } else {
                sortStr = "";
            }
        } else if (queryMode == 2) {
            if (!StringUtils.isEmpty(sortFields)) {
                sortStr = String.format("order by %s", sortFields);
            }
        }
        String mainSql = String.format("select %s from %s where %s %s", queryFields, joinCondition, where, sortStr);
        return mainSql;
    }

    /**
     * 分组统计
     *
     * @param queryParam
     * @return
     */
    public String queryGroupStatistics(QueryParamEntity queryParam) {
        String statisticsType, joinCondition, where, groupFields;
        if (queryParam.getStatisticsValueType() == 1) {
            statisticsType = "count(1)";
        } else {
            statisticsType = String.format("sum(%s)", queryParam.getStatisticsSumField());
        }
        joinCondition = queryParam.getJoinCondition();
        where = queryParam.getWhere();
        // 拼接完整sql语句
        String mainSql = "";
        if (StringUtils.isEmpty(queryParam.getStatisticsColumnField())) {
            groupFields = queryParam.getStatisticsRowField();
            mainSql = String.format("select %s as r,%s as v from %s where %s group by %s", queryParam.getStatisticsRowField(), statisticsType, joinCondition, where, groupFields);
        } else {
            groupFields = String.format("%s,%s", queryParam.getStatisticsRowField(), queryParam.getStatisticsColumnField());
            mainSql = String.format("select %s as r,%s as c,%s as v from %s where %s group by %s", queryParam.getStatisticsRowField(),
                    queryParam.getStatisticsColumnField(), statisticsType, joinCondition, where, groupFields);
        }
        return mainSql;
    }

    public String queryCount(QueryParamEntity queryParam) {
        String joinCondition, where;
        joinCondition = queryParam.getJoinCondition();
        where = queryParam.getWhere();
        //5、拼接完整sql语句
        String mainSql = String.format("select count(*) from %s where %s", joinCondition, where);
        return mainSql;
    }

    /**
     * 通用的执行sql语句的方法，切记不能拼接sql
     *
     * @param sqlEntity
     * @return
     */
    public String executeUpdate(SqlEntity sqlEntity) {
        String sql = sqlEntity.getSqlStr();
        if (StringUtils.isEmpty(sql)) {
            //TODO: 记录日志
            log.warn("sql is null");
            sql = "";
        }
        return sql;
    }

    public String executeQuerySql(SqlEntity sqlEntity) {
        String sql = sqlEntity.getSqlStr();
        if (StringUtils.isEmpty(sql)) {
            //TODO: 记录日志
        }
        return sql;
    }
}
