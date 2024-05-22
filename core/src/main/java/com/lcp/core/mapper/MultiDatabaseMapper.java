package com.lcp.core.mapper;

import com.lcp.core.model.auto.DmDatabaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 多数据库测试mapper
 */
@Mapper
public interface MultiDatabaseMapper {
    /**
     * 获取当前数据库连接所有使用的库名字
     *
     * @return
     */
    @Select("Select Name From Master..SysDataBases Where DbId=(Select Dbid From Master..SysProcesses Where Spid = @@spid)")
    String getCurrentDatabaseName();

    /**
     * 获取数据库配置信息
     *
     * @return
     */
    @Select("select * from dm_database where id in(select database_Id from ss_app where id=#{appId})")
    List<DmDatabaseEntity> getDatabaseSettingByAppId(Long appId);

    /**
     * 主框架模式运行下需要获取所有的数据库连接信息
     *
     * @return
     */
    @Select("select * from dm_database where id in(select database_Id from ss_app where deploy_type=0)")
    List<DmDatabaseEntity> getAllDatabaseSetting();

    /**
     * 获取应用的数量
     *
     * @return
     */
    @Select("select count(*) from ss_app")
    Integer getAppCount();
}
