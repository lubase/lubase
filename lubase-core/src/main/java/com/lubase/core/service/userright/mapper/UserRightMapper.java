package com.lubase.core.service.userright.mapper;

import com.lubase.model.DbEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface UserRightMapper {

    @Select("call proc_getRoleListByOrgId(#{orgId})")
    @Options(statementType = StatementType.CALLABLE)
    List<String> getRoleListByOrgId(String orgId);

    @Select("select app_id,role_type from sa_role where id=#{roleId}")
    DbEntity getRoleInfoById(Long roleId);

    @Select("select func_id from sa_func_assign where role_id=#{roleId}")
    List<Long> getRoleFuncList(Long roleId);

    @Select("select id,table_id,column_id,access_right from sa_column_assign where role_id=#{roleId}")
    List<DbEntity> getRoleColList(Long roleId);

    @Select("select role_id from sa_role_assign where account_id=#{userId}")
    List<DbEntity> getUserAssignRoleList(Long userId);

    @Select("select organization_id from sa_account where id=#{userId}")
    Long getUserOrgId(Long userId);

    @Select("select id,role_type,public_role,app_id from sa_role where public_role=1 or id in(${roleIds})")
    List<DbEntity> getUserRoleList(String roleIds);
}
