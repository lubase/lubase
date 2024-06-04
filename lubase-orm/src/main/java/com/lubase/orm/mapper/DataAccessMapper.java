package com.lubase.orm.mapper;

import com.lubase.orm.model.QueryParamEntity;
import com.lubase.orm.model.SqlEntity;
import com.lubase.orm.model.statistics.StatisticsEntity;
import com.lubase.model.DbEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 数据库通用查询
 *
 * @author A
 */
@Mapper
public interface DataAccessMapper {
    /**
     * 当数据库出现未提交事务时，显示出是哪个库
     *
     * @return
     */
    @Select("select monster_name from monster")
    String getMonster();

    /**
     * 查询列表
     *
     * @param queryParam
     * @return
     */
    @SelectProvider(type = DataAccessSqlBuilder.class, method = "queryList")
    List<DbEntity> executeQueryList(QueryParamEntity queryParam);

    /**
     * 执行分组统计
     *
     * @param queryParam
     * @return
     */
    @SelectProvider(type = DataAccessSqlBuilder.class, method = "queryGroupStatistics")
    List<StatisticsEntity> executeQueryGroupStatistics(QueryParamEntity queryParam);

    /**
     * 查询记录数
     *
     * @param queryParam 参数集合
     * @return
     */
    @SelectProvider(type = DataAccessSqlBuilder.class, method = "queryCount")
    int executeQueryCount(QueryParamEntity queryParam);

    /**
     * 执行update方法
     *
     * @param sqlEntity
     * @return
     */
    @UpdateProvider(type = DataAccessSqlBuilder.class, method = "executeUpdate")
    int executeUpdate(SqlEntity sqlEntity);

    /**
     * 执行sql语句进行查询，返回列表
     *
     * @param sqlEntity
     * @return
     */
    @SelectProvider(type = DataAccessSqlBuilder.class, method = "executeQuerySql")
    List<DbEntity> executeSqlResList(SqlEntity sqlEntity);

    /**
     * 执行sql语句进行查询，返回对象
     *
     * @param sqlEntity
     * @return
     */
    @SelectProvider(type = DataAccessSqlBuilder.class, method = "executeQuerySql")
    DbEntity executeSqlResDbEntity(SqlEntity sqlEntity);
}
