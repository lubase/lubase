package com.lubase.core.service.userright.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface UserOrgRightMapper {

    @Select("call proc_getRoleListByOrgId(#{orgId})")
    @Options(statementType = StatementType.CALLABLE)
    List<String> getRoleListByOrgId(String orgId);
}
