package com.lubase.cache.mapper;

import com.lubase.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 此mapper 封装 dataAccess 用到的所有的查询
 */
@Mapper
public interface CacheCoreTableMapper {

    /**
     * 获取注册的表信息，后续应该优先使用此方法
     *
     * @param id
     * @return
     */
    DbTable initTableInfoById(Long id);

    /**
     * 获取数据库表code和表id对应关系。仅用于缓存初始化时获取所有的表信息，请勿使用到其他地方
     *
     * @return
     */
    List<DbTable> getTableCodeAndIdInfo();

    /**
     * 根据表id获取表code
     *
     * @param tableId
     * @return
     */
    @Select("select  table_code from dm_table where id=#{tableId}")
    String getTableCodeById(Long tableId);

    /**
     * 根据表code获取表id
     *
     * @param tableCode
     * @return
     */
    @Select("select  id from dm_table where table_code=#{tableCode}")
    Long getTableIdByCode(String tableCode);

    /**
     * 获取注册列信息
     *
     * @param tableId
     * @return
     */
    List<DbField> initColumnInfoByTableId(Long tableId);

    /**
     * 根据id获取列信息
     *
     * @param id
     * @return
     */
    DbField initColumnInfoById(Long id);


    /**
     * 根据代码表类型获取代码表数据
     *
     * @param typeId
     * @return
     */
    List<DbCode> getCodeListByTypeId(String typeId);

    /**
     * 获取表缓存配置
     *
     * @param tableCode
     * @return
     */
    @Select("select * from ss_cache where table_code=#{tableCode}")
    List<SsCacheEntity> getTableCacheSetting(String tableCode);

    @Select("select * from ss_cache")
    List<SsCacheEntity> getTableCacheList();

    @Select("select * from dm_relate_update")
    List<DbEntity> getTableRelateList();

    @Select("select table_id from sa_controlled_table")
    List<String> getControlledTableList();

    @Select("select id,file_name from sd_upload_file where data_id=#{dataId} and file_key=#{fileKey}")
    List<DbEntity> getFileDisplayNameById(@Param("dataId") String dataId, @Param("fileKey") String fileKey);

    List<ResourceDataModel> getResourceList(@Param("appId") String appId);
}
