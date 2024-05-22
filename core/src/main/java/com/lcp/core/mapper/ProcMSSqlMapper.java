package com.lcp.core.mapper;

import com.lcp.coremodel.DbEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface ProcMSSqlMapper {
    @SelectProvider(type = ProcSqlBuilder.class, method = "getDataList")
    @Options(statementType = StatementType.CALLABLE)
    List<DbEntity> getDbEntityList(String procName, String... p1);

    @SelectProvider(type = ProcSqlBuilder.class, method = "getDataList")
    @Options(statementType = StatementType.CALLABLE)
    List<String> getStringList(String procName, String... p1);

}
