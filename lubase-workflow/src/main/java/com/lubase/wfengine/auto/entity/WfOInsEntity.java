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
* @since 2022-11-27
*/
    @TableName("wf_oins")
    public class WfOInsEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_oins";
    public static final String COL_ID = "id";
    public static final String COL_SERVICE_ID = "service_id";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_FINS_ID = "fins_id";
    public static final String COL_TASK_ID = "task_id";
    public static final String COL_TASK_NAME = "task_name";
    public static final String COL_TINS_ID = "tins_id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_PROCESS_TIME = "process_time";
    public static final String COL_PROCESS_STATUS = "process_status";
    public static final String COL_PROCESS_CMD_ID = "process_cmd_id";
    public static final String COL_PROCESS_MEMO = "process_memo";
    public static final String COL_PROCESS_CMD_DES = "process_cmd_des";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";

    public String getService_id() {
        return TypeConverterUtils.object2String(super.get("service_id"));
    }

    public void setService_id(String service_id) {
        super.put("service_id",service_id);
    }
    public String getFlow_id() {
        return TypeConverterUtils.object2String(super.get("flow_id"));
    }

    public void setFlow_id(String flow_id) {
        super.put("flow_id",flow_id);
    }
    public String getFins_id() {
        return TypeConverterUtils.object2String(super.get("fins_id"));
    }

    public void setFins_id(String fins_id) {
        super.put("fins_id",fins_id);
    }
    public String getTask_id() {
        return TypeConverterUtils.object2String(super.get("task_id"));
    }

    public void setTask_id(String task_id) {
        super.put("task_id",task_id);
    }
    public String getTask_name() {
        return TypeConverterUtils.object2String(super.get("task_name"));
    }

    public void setTask_name(String task_name) {
        super.put("task_name",task_name);
    }
    public String getTins_id() {
        return TypeConverterUtils.object2String(super.get("tins_id"));
    }

    public void setTins_id(String tins_id) {
        super.put("tins_id",tins_id);
    }
    public String getUser_id() {
        return TypeConverterUtils.object2String(super.get("user_id"));
    }

    public void setUser_id(String user_id) {
        super.put("user_id",user_id);
    }
    public String getUser_name() {
        return TypeConverterUtils.object2String(super.get("user_name"));
    }

    public void setUser_name(String user_name) {
        super.put("user_name",user_name);
    }
    public LocalDateTime getStart_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("start_time"));
    }

    public void setStart_time(LocalDateTime start_time) {
        super.put("start_time",start_time);
    }
    public LocalDateTime getProcess_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("process_time"));
    }

    public void setProcess_time(LocalDateTime process_time) {
        super.put("process_time",process_time);
    }
    public String getProcess_status() {
        return TypeConverterUtils.object2String(super.get("process_status"));
    }

    public void setProcess_status(String process_status) {
        super.put("process_status",process_status);
    }
    public String getProcess_cmd_id() {
        return TypeConverterUtils.object2String(super.get("process_cmd_id"));
    }

    public void setProcess_cmd_id(String process_cmd_id) {
        super.put("process_cmd_id",process_cmd_id);
    }
    public String getProcess_memo() {
        return TypeConverterUtils.object2String(super.get("process_memo"));
    }

    public void setProcess_memo(String process_memo) {
        super.put("process_memo",process_memo);
    }
    public String getProcess_cmd_des() {
        return TypeConverterUtils.object2String(super.get("process_cmd_des"));
    }

    public void setProcess_cmd_des(String process_cmd_des) {
        super.put("process_cmd_des",process_cmd_des);
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
        return "WfOInsEntity2{" +
                "ID=" + getId() +
                ", SERVICE_ID=" + getService_id() +
                ", FLOW_ID=" + getFlow_id() +
                ", FINS_ID=" + getFins_id() +
                ", TASK_ID=" + getTask_id() +
                ", TASK_NAME=" + getTask_name() +
                ", TINS_ID=" + getTins_id() +
                ", USER_ID=" + getUser_id() +
                ", USER_NAME=" + getUser_name() +
                ", START_TIME=" + getStart_time() +
                ", PROCESS_TIME=" + getProcess_time() +
                ", PROCESS_STATUS=" + getProcess_status() +
                ", PROCESS_CMD_ID=" + getProcess_cmd_id() +
                ", PROCESS_MEMO=" + getProcess_memo() +
                ", PROCESS_CMD_DES=" + getProcess_cmd_des() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                "}";
    }
}
