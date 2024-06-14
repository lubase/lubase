package com.lubase.orm.mapper;

import com.lubase.model.DbEntity;
import com.lubase.orm.model.EDatabaseType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface ProcMSSqlMapper {
    @SelectProvider(type = ProcSqlBuilder.class, method = "getDataList")
    @Options(statementType = StatementType.CALLABLE)
    List<DbEntity> getDbEntityList(EDatabaseType databaseType, String procName, String... p1);

    @SelectProvider(type = ProcSqlBuilder.class, method = "getDataList")
    @Options(statementType = StatementType.CALLABLE)
    List<String> getStringList(EDatabaseType databaseType, String procName, String... p1);
}
