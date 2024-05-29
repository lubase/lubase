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
* @since 2023-07-02
*/
    @TableName("wf_tevent")
    public class WfTEventEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "wf_tevent";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_FINS_ID = "fins_id";
    public static final String COL_TASK_ID = "task_id";
    public static final String COL_STATUS = "status";
    public static final String COL_PROCESS_TIME = "process_time";
    public static final String COL_TINS_ID = "tins_id";
    public static final String COL_ERROR_COUNT = "error_count";
    public static final String COL_ERROR_TIP = "error_tip";

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
        public String getStatus() {
            return TypeConverterUtils.object2String(super.get("status"));
        }

            public void setStatus(String status) {
        super.put("status",status);
        }
        public LocalDateTime getProcess_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("process_time"));
        }

            public void setProcess_time(LocalDateTime process_time) {
        super.put("process_time",process_time);
        }
        public String getTins_id() {
            return TypeConverterUtils.object2String(super.get("tins_id"));
        }

            public void setTins_id(String tins_id) {
        super.put("tins_id",tins_id);
        }
        public Integer getError_count() {
            return TypeConverterUtils.object2Integer(super.get("error_count"));
        }

            public void setError_count(Integer error_count) {
        super.put("error_count",error_count);
        }
        public String getError_tip() {
            return TypeConverterUtils.object2String(super.get("error_tip"));
        }

            public void setError_tip(String error_tip) {
        super.put("error_tip",error_tip);
        }

    @Override
    public String toString() {
    return "WfTEventEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", FLOW_ID=" + getFlow_id() +
            ", FINS_ID=" + getFins_id() +
            ", TASK_ID=" + getTask_id() +
            ", STATUS=" + getStatus() +
            ", PROCESS_TIME=" + getProcess_time() +
            ", TINS_ID=" + getTins_id() +
            ", ERROR_COUNT=" + getError_count() +
            ", ERROR_TIP=" + getError_tip() +
    "}";
    }
}
