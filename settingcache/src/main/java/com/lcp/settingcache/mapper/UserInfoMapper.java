package com.lcp.settingcache.mapper;

import com.lcp.coremodel.DbEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {
    /**
     * 根据用户id获取用户信息
     *
     * @param id
     * @return
     */
    @Select("select id,user_name,user_code,organization_id from sa_account where id=#{id}")
    DbEntity getUserInfoById(Long id);

    /**
     * 根据登录工号获取用户信息
     *
     * @param userCode
     * @return
     */
    @Select("select id,user_name,user_code,organization_id from sa_account where user_code=#{userCode}")
    DbEntity getUserInfoByCode(String userCode);

    /**
     * 根据部门id获取部门信息
     *
     * @param id
     * @return
     */
    @Select("select id,org_name from sa_organization where id=#{id}")
    DbEntity getDeptInfoByCode(Long id);
}
