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
    @TableName("wf_cmd")
    public class WfCmdEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_cmd";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_TASK_ID = "task_id";
    public static final String COL_CMD_TYPE = "cmd_type";
    public static final String COL_CMD_DES = "cmd_des";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_UPDATE_CONTENT = "update_content";
    public static final String COL_REQUIRE_PROCESS_MEMO = "require_process_memo";
    public static final String COL_TIMEOUT_PROCESS = "timeout_process";

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
    public String getTask_id() {
        return TypeConverterUtils.object2String(super.get("task_id"));
    }

    public void setTask_id(String task_id) {
        super.put("task_id",task_id);
    }
    public String getCmd_type() {
        return TypeConverterUtils.object2String(super.get("cmd_type"));
    }

    public void setCmd_type(String cmd_type) {
        super.put("cmd_type",cmd_type);
    }
    public String getCmd_des() {
        return TypeConverterUtils.object2String(super.get("cmd_des"));
    }

    public void setCmd_des(String cmd_des) {
        super.put("cmd_des",cmd_des);
    }
    public Integer getOrder_id() {
        return TypeConverterUtils.object2Integer(super.get("order_id"));
    }

    public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
    }
    public String getUpdate_content() {
        return TypeConverterUtils.object2String(super.get("update_content"));
    }

    public void setUpdate_content(String update_content) {
        super.put("update_content",update_content);
    }
    public Boolean getRequire_process_memo() {
        return TypeConverterUtils.object2Boolean(super.get("require_process_memo"));
    }

    public void setRequire_process_memo(Boolean require_process_memo) {
        super.put("require_process_memo",require_process_memo);
    }
    public Boolean getTimeout_process() {
        return TypeConverterUtils.object2Boolean(super.get("timeout_process"));
    }

    public void setTimeout_process(Boolean timeout_process) {
        super.put("timeout_process",timeout_process);
    }

    @Override
    public String toString() {
        return "WfCmdEntity2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", TASK_ID=" + getTask_id() +
                ", CMD_TYPE=" + getCmd_type() +
                ", CMD_DES=" + getCmd_des() +
                ", ORDER_ID=" + getOrder_id() +
                ", UPDATE_CONTENT=" + getUpdate_content() +
                ", REQUIRE_PROCESS_MEMO=" + getRequire_process_memo() +
                ", TIMEOUT_PROCESS=" + getTimeout_process() +
                "}";
    }
}
