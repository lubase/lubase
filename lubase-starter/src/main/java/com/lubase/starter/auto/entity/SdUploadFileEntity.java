package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 系统附件信息
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("sd_upload_file")
    public class SdUploadFileEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "sd_upload_file";
    public static final String COL_ID = "id";
    public static final String COL_FILE_NAME = "file_name";
    public static final String COL_EX_TYPE = "ex_type";
    public static final String COL_FILE_PATH = "file_path";
    public static final String COL_DATA_ID = "data_id";
    public static final String COL_FILE_KEY = "file_key";
    public static final String COL_MD5 = "md5";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";

        public String getFile_name() {
            return TypeConverterUtils.object2String(super.get("file_name"));
        }

            public void setFile_name(String file_name) {
        super.put("file_name",file_name);
        }
        public String getEx_type() {
            return TypeConverterUtils.object2String(super.get("ex_type"));
        }

            public void setEx_type(String ex_type) {
        super.put("ex_type",ex_type);
        }
        public String getFile_path() {
            return TypeConverterUtils.object2String(super.get("file_path"));
        }

            public void setFile_path(String file_path) {
        super.put("file_path",file_path);
        }
        public Long getData_id() {
            return TypeConverterUtils.object2Long(super.get("data_id"));
        }

            public void setData_id(Long data_id) {
        super.put("data_id",data_id);
        }
        public String getFile_key() {
            return TypeConverterUtils.object2String(super.get("file_key"));
        }

            public void setFile_key(String file_key) {
        super.put("file_key",file_key);
        }
        public String getMd5() {
            return TypeConverterUtils.object2String(super.get("md5"));
        }

            public void setMd5(String md5) {
        super.put("md5",md5);
        }
        public Boolean getDelete_tag() {
            return TypeConverterUtils.object2Boolean(super.get("delete_tag"));
        }

            public void setDelete_tag(Boolean delete_tag) {
        super.put("delete_tag",delete_tag);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }
        public Long getCreated_by() {
            return TypeConverterUtils.object2Long(super.get("created_by"));
        }

            public void setCreated_by(Long created_by) {
        super.put("created_by",created_by);
        }
        public LocalDateTime getCreated_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("created_time"));
        }

            public void setCreated_time(LocalDateTime created_time) {
        super.put("created_time",created_time);
        }
        public Long getUpdated_by() {
            return TypeConverterUtils.object2Long(super.get("updated_by"));
        }

            public void setUpdated_by(Long updated_by) {
        super.put("updated_by",updated_by);
        }
        public LocalDateTime getUpdated_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("updated_time"));
        }

            public void setUpdated_time(LocalDateTime updated_time) {
        super.put("updated_time",updated_time);
        }

    @Override
    public String toString() {
    return "SdUploadFileEntity{" +
            "ID=" + getId() +
            ", FILE_NAME=" + getFile_name() +
            ", EX_TYPE=" + getEx_type() +
            ", FILE_PATH=" + getFile_path() +
            ", DATA_ID=" + getData_id() +
            ", FILE_KEY=" + getFile_key() +
            ", MD5=" + getMd5() +
            ", DELETE_TAG=" + getDelete_tag() +
            ", ORDER_ID=" + getOrder_id() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
    "}";
    }
}
