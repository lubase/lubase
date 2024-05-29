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
    @TableName("sd_file_info")
    public class SdFileInfoEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "sd_file_info";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_STORAGE_SERVICE = "storage_service";
    public static final String COL_FILE_PATH = "file_path";
    public static final String COL_FILE_NAME = "file_name";
    public static final String COL_ORIGINAL_NAME = "original_name";
    public static final String COL_FILE_SIZE = "file_size";
    public static final String COL_EX_TYPE = "ex_type";
    public static final String COL_MD5 = "md5";

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
        public String getStorage_service() {
            return TypeConverterUtils.object2String(super.get("storage_service"));
        }

            public void setStorage_service(String storage_service) {
        super.put("storage_service",storage_service);
        }
        public String getFile_path() {
            return TypeConverterUtils.object2String(super.get("file_path"));
        }

            public void setFile_path(String file_path) {
        super.put("file_path",file_path);
        }
        public String getFile_name() {
            return TypeConverterUtils.object2String(super.get("file_name"));
        }

            public void setFile_name(String file_name) {
        super.put("file_name",file_name);
        }
        public String getOriginal_name() {
            return TypeConverterUtils.object2String(super.get("original_name"));
        }

            public void setOriginal_name(String original_name) {
        super.put("original_name",original_name);
        }
        public Integer getFile_size() {
            return TypeConverterUtils.object2Integer(super.get("file_size"));
        }

            public void setFile_size(Integer file_size) {
        super.put("file_size",file_size);
        }
        public String getEx_type() {
            return TypeConverterUtils.object2String(super.get("ex_type"));
        }

            public void setEx_type(String ex_type) {
        super.put("ex_type",ex_type);
        }
        public String getMd5() {
            return TypeConverterUtils.object2String(super.get("md5"));
        }

            public void setMd5(String md5) {
        super.put("md5",md5);
        }

    @Override
    public String toString() {
    return "SdFileInfoEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", STORAGE_SERVICE=" + getStorage_service() +
            ", FILE_PATH=" + getFile_path() +
            ", FILE_NAME=" + getFile_name() +
            ", ORIGINAL_NAME=" + getOriginal_name() +
            ", FILE_SIZE=" + getFile_size() +
            ", EX_TYPE=" + getEx_type() +
            ", MD5=" + getMd5() +
    "}";
    }
}
