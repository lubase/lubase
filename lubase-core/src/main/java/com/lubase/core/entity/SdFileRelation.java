package com.lubase.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 
    * </p>
*
* @author A
* @since 2023-07-03
*/
    @TableName("sd_file_relation")
    public class SdFileRelation extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "sd_file_relation";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_APP_ID = "app_id";
    public static final String COL_DATA_ID = "data_id";
    public static final String COL_FILE_INFO_ID = "file_info_id";
    public static final String COL_DATA_COLUMN_TAG = "data_column_tag";
    public static final String COL_ORIGINAL_NAME = "original_name";

    public Long getCreate_by() {
        return TypeConverterUtils.object2Long(super.get("create_by"));
    }

    public void setCreate_by(Long create_by) {
        super.put("create_by",create_by);
    }
    public LocalDateTime getCreate_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("create_time"));
    }

    public void setCreate_time(LocalDateTime create_time) {
        super.put("create_time",create_time);
    }
    public Long getUpdate_by() {
        return TypeConverterUtils.object2Long(super.get("update_by"));
    }

    public void setUpdate_by(Long update_by) {
        super.put("update_by",update_by);
    }
    public LocalDateTime getUpdate_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("update_time"));
    }

    public void setUpdate_time(LocalDateTime update_time) {
        super.put("update_time",update_time);
    }
    public Long getApp_id() {
        return TypeConverterUtils.object2Long(super.get("app_id"));
    }

    public void setApp_id(Long app_id) {
        super.put("app_id",app_id);
    }
    public String getData_id() {
        return TypeConverterUtils.object2String(super.get("data_id"));
    }

    public void setData_id(String data_id) {
        super.put("data_id",data_id);
    }
    public String getFile_info_id() {
        return TypeConverterUtils.object2String(super.get("file_info_id"));
    }

    public void setFile_info_id(String file_info_id) {
        super.put("file_info_id",file_info_id);
    }
    public String getData_column_tag() {
        return TypeConverterUtils.object2String(super.get("data_column_tag"));
    }

    public void setData_column_tag(String data_column_tag) {
        super.put("data_column_tag",data_column_tag);
    }
    public String getOriginal_name() {
        return TypeConverterUtils.object2String(super.get("original_name"));
    }

    public void setOriginal_name(String original_name) {
        super.put("original_name",original_name);
    }

    @Override
    public String toString() {
        return "SdFileRelation2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", APP_ID=" + getApp_id() +
                ", DATA_ID=" + getData_id() +
                ", FILE_INFO_ID=" + getFile_info_id() +
                ", DATA_COLUMN_TAG=" + getData_column_tag() +
                ", ORIGINAL_NAME=" + getOriginal_name() +
                "}";
    }
}
