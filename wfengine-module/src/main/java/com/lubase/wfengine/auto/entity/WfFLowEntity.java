package com.lubase.wfengine.auto.entity;

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
* @author shishuai
* @since 2022-11-27
*/
    @TableName("wf_flow")
    public class WfFLowEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_flow";
    public static final String COL_ID = "id";
    public static final String COL_SERVICE_ID = "service_id";
    public static final String COL_VERSION = "version";
    public static final String COL_RELEASE_STATUS = "release_status";
    public static final String COL_RELEASE_TIME = "release_time";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FORM_ID = "form_id";

    public String getService_id() {
        return TypeConverterUtils.object2String(super.get("service_id"));
    }

    public void setService_id(String service_id) {
        super.put("service_id",service_id);
    }
    public Integer getVersion() {
        return TypeConverterUtils.object2Integer(super.get("version"));
    }

    public void setVersion(Integer version) {
        super.put("version",version);
    }
    public Integer getRelease_status() {
        return TypeConverterUtils.object2Integer(super.get("release_status"));
    }

    public void setRelease_status(Integer release_status) {
        super.put("release_status",release_status);
    }
    public LocalDateTime getRelease_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("release_time"));
    }

    public void setRelease_time(LocalDateTime release_time) {
        super.put("release_time",release_time);
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
    public String getForm_id() {
        return TypeConverterUtils.object2String(super.get("form_id"));
    }

    public void setForm_id(String form_id) {
        super.put("form_id",form_id);
    }

    @Override
    public String toString() {
        return "WfFLowEntity2{" +
                "ID=" + getId() +
                ", SERVICE_ID=" + getService_id() +
                ", VERSION=" + getVersion() +
                ", RELEASE_STATUS=" + getRelease_status() +
                ", RELEASE_TIME=" + getRelease_time() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", FORM_ID=" + getForm_id() +
                "}";
    }
}
