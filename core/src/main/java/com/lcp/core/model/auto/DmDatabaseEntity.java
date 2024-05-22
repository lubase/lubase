package com.lcp.core.model.auto;

import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 数据库表
    * </p>
*
* @author A
* @since 2022-06-03
*/
    public class DmDatabaseEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "dm_database";
    public static final String COL_ID = "id";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_PASSWORD = "password";
    public static final String COL_DATABASE_TYPE = "database_type";
    public static final String COL_DRIVE_CLASS = "drive_class";
    public static final String COL_DATABASE_NAME = "database_name";
    public static final String COL_DATABASE_DESCRIPTION = "database_description";
    public static final String COL_TEST_SQL = "test_sql";
    public static final String COL_PORT = "port";
    public static final String COL_INSTANCE_NAME = "instance_name";
    public static final String COL_HOST = "host";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";

    public String getUser_name() {
        return TypeConverterUtils.object2String(super.get("user_name"));
    }

    public void setUser_name(String user_name) {
        super.put("user_name",user_name);
    }
    public String getPassword() {
        return TypeConverterUtils.object2String(super.get("password"));
    }

    public void setPassword(String password) {
        super.put("password",password);
    }
    public String getDatabase_type() {
        return TypeConverterUtils.object2String(super.get("database_type"));
    }

    public void setDatabase_type(String database_type) {
        super.put("database_type",database_type);
    }
    public String getDrive_class() {
        return TypeConverterUtils.object2String(super.get("drive_class"));
    }

    public void setDrive_class(String drive_class) {
        super.put("drive_class",drive_class);
    }
    public String getDatabase_name() {
        return TypeConverterUtils.object2String(super.get("database_name"));
    }

    public void setDatabase_name(String database_name) {
        super.put("database_name",database_name);
    }
    public String getDatabase_description() {
        return TypeConverterUtils.object2String(super.get("database_description"));
    }

    public void setDatabase_description(String database_description) {
        super.put("database_description",database_description);
    }
    public String getTest_sql() {
        return TypeConverterUtils.object2String(super.get("test_sql"));
    }

    public void setTest_sql(String test_sql) {
        super.put("test_sql",test_sql);
    }
    public Integer getPort() {
        return TypeConverterUtils.object2Integer(super.get("port"));
    }

    public void setPort(Integer port) {
        super.put("port",port);
    }
    public String getInstance_name() {
        return TypeConverterUtils.object2String(super.get("instance_name"));
    }

    public void setInstance_name(String instance_name) {
        super.put("instance_name",instance_name);
    }
    public String getHost() {
        return TypeConverterUtils.object2String(super.get("host"));
    }

    public void setHost(String host) {
        super.put("host",host);
    }
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

    @Override
    public String toString() {
        return "DmDatabaseEntity2{" +
                "ID=" + getId() +
                ", USER_NAME=" + getUser_name() +
                ", PASSWORD=" + getPassword() +
                ", DATABASE_TYPE=" + getDatabase_type() +
                ", DRIVE_CLASS=" + getDrive_class() +
                ", DATABASE_NAME=" + getDatabase_name() +
                ", DATABASE_DESCRIPTION=" + getDatabase_description() +
                ", TEST_SQL=" + getTest_sql() +
                ", PORT=" + getPort() +
                ", INSTANCE_NAME=" + getInstance_name() +
                ", HOST=" + getHost() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                "}";
    }
}
