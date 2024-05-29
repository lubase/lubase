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
    @TableName("wf_tins")
    public class WfTInsEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "wf_tins";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_FINS_ID = "fins_id";
    public static final String COL_TASK_ID = "task_id";
    public static final String COL_TASK_NAME = "task_name";
    public static final String COL_PRE_TASK_ID = "pre_task_id";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_DEAD_LINE = "dead_line";
    public static final String COL_PROCESS_TIME = "process_time";
    public static final String COL_PROCESS_STATUS = "process_status";

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
        public String getPre_task_id() {
            return TypeConverterUtils.object2String(super.get("pre_task_id"));
        }

            public void setPre_task_id(String pre_task_id) {
        super.put("pre_task_id",pre_task_id);
        }
        public LocalDateTime getStart_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("start_time"));
        }

            public void setStart_time(LocalDateTime start_time) {
        super.put("start_time",start_time);
        }
        public LocalDateTime getDead_line() {
            return TypeConverterUtils.object2LocalDateTime(super.get("dead_line"));
        }

            public void setDead_line(LocalDateTime dead_line) {
        super.put("dead_line",dead_line);
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

    @Override
    public String toString() {
    return "WfTInsEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", FLOW_ID=" + getFlow_id() +
            ", FINS_ID=" + getFins_id() +
            ", TASK_ID=" + getTask_id() +
            ", TASK_NAME=" + getTask_name() +
            ", PRE_TASK_ID=" + getPre_task_id() +
            ", START_TIME=" + getStart_time() +
            ", DEAD_LINE=" + getDead_line() +
            ", PROCESS_TIME=" + getProcess_time() +
            ", PROCESS_STATUS=" + getProcess_status() +
    "}";
    }
}
