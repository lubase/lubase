package com.lubase.cache.mapper;

import com.lubase.model.DbEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileInfoMapper {

    @Select("select id,original_name file_name from sd_file_relation where data_id=#{dataId} and data_column_tag=#{dataColumnTag}")
    List<DbEntity> getFileDisplayNameById(@Param("dataId") String dataId, @Param("dataColumnTag") String dataColumnTag);
}
