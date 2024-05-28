package com.lubase.wfengine.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 
    * </p>
*
* @author shishuai
* @since 2023-02-08
*/
    @TableName("wf_callback")
    public class WfCallbackEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_callback";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FINS_ID = "fins_id";
    public static final String COL_DATA_ID = "data_id";
    public static final String COL_UPDATE_TYPE = "update_type";
    public static final String COL_UPDATE_CONTENT = "update_content";
    public static final String COL_PROCESS_TIME = "process_time";
    public static final String COL_STATUS = "status";
    public static final String COL_SERVICE_ID = "service_id";
    public static final String COL_UPDATE_MEMO = "update_memo";

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
    public String getFins_id() {
        return TypeConverterUtils.object2String(super.get("fins_id"));
    }

    public void setFins_id(String fins_id) {
        super.put("fins_id",fins_id);
    }
    public String getData_id() {
        return TypeConverterUtils.object2String(super.get("data_id"));
    }

    public void setData_id(String data_id) {
        super.put("data_id",data_id);
    }
    public String getUpdate_type() {
        return TypeConverterUtils.object2String(super.get("update_type"));
    }

    public void setUpdate_type(String update_type) {
        super.put("update_type",update_type);
    }
    public String getUpdate_content() {
        return TypeConverterUtils.object2String(super.get("update_content"));
    }

    public void setUpdate_content(String update_content) {
        super.put("update_content",update_content);
    }
    public LocalDateTime getProcess_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("process_time"));
    }

    public void setProcess_time(LocalDateTime process_time) {
        super.put("process_time",process_time);
    }
    public String getStatus() {
        return TypeConverterUtils.object2String(super.get("status"));
    }

    public void setStatus(String status) {
        super.put("status",status);
    }
    public String getService_id() {
        return TypeConverterUtils.object2String(super.get("service_id"));
    }

    public void setService_id(String service_id) {
        super.put("service_id",service_id);
    }
    public String getUpdate_memo() {
        return TypeConverterUtils.object2String(super.get("update_memo"));
    }

    public void setUpdate_memo(String update_memo) {
        super.put("update_memo",update_memo);
    }

    @Override
    public String toString() {
        return "WfCallbackEntity2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", FINS_ID=" + getFins_id() +
                ", DATA_ID=" + getData_id() +
                ", UPDATE_TYPE=" + getUpdate_type() +
                ", UPDATE_CONTENT=" + getUpdate_content() +
                ", PROCESS_TIME=" + getProcess_time() +
                ", STATUS=" + getStatus() +
                ", SERVICE_ID=" + getService_id() +
                ", UPDATE_MEMO=" + getUpdate_memo() +
                "}";
    }
}
