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
    @TableName("wf_fins")
    public class WfFInsEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "wf_fins";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_SERVICE_ID = "service_id";
    public static final String COL_SERVICE_NAME = "service_name";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_DATA_ID = "data_id";
    public static final String COL_NAME = "name";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String COL_APPROVAL_STATUS = "approval_status";
    public static final String COL_C_TASK_ID = "c_task_id";
    public static final String COL_C_TASK_NAME = "c_task_name";
    public static final String COL_C_TINS_ID = "c_tins_id";
    public static final String COL_PROCESS_USER_ID = "process_user_id";

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
        public String getService_id() {
            return TypeConverterUtils.object2String(super.get("service_id"));
        }

            public void setService_id(String service_id) {
        super.put("service_id",service_id);
        }
    public String getService_name() {
        return TypeConverterUtils.object2String(super.get("service_name"));
    }

    public void setService_name(String service_name) {
        super.put("service_name",service_name);
    }
        public String getFlow_id() {
            return TypeConverterUtils.object2String(super.get("flow_id"));
        }

            public void setFlow_id(String flow_id) {
        super.put("flow_id",flow_id);
        }
        public String getData_id() {
            return TypeConverterUtils.object2String(super.get("data_id"));
        }

            public void setData_id(String data_id) {
        super.put("data_id",data_id);
        }
        public String getName() {
            return TypeConverterUtils.object2String(super.get("name"));
        }

            public void setName(String name) {
        super.put("name",name);
        }
        public LocalDateTime getStart_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("start_time"));
        }

            public void setStart_time(LocalDateTime start_time) {
        super.put("start_time",start_time);
        }
        public LocalDateTime getEnd_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("end_time"));
        }

            public void setEnd_time(LocalDateTime end_time) {
        super.put("end_time",end_time);
        }
        public String getApproval_status() {
            return TypeConverterUtils.object2String(super.get("approval_status"));
        }

            public void setApproval_status(String approval_status) {
        super.put("approval_status",approval_status);
        }
        public String getc_task_id() {
            return TypeConverterUtils.object2String(super.get("c_task_id"));
        }

            public void setc_task_id(String c_task_id) {
        super.put("c_task_id",c_task_id);
        }
        public String getc_task_name() {
            return TypeConverterUtils.object2String(super.get("c_task_name"));
        }

            public void setc_task_name(String c_task_name) {
        super.put("c_task_name",c_task_name);
        }
        public String getc_tins_id() {
            return TypeConverterUtils.object2String(super.get("c_tins_id"));
        }

            public void setc_tins_id(String c_tins_id) {
        super.put("c_tins_id",c_tins_id);
        }
        public String getProcess_user_id() {
            return TypeConverterUtils.object2String(super.get("process_user_id"));
        }

            public void setProcess_user_id(String process_user_id) {
        super.put("process_user_id",process_user_id);
        }

    @Override
    public String toString() {
    return "WfFInsEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", SERVICE_ID=" + getService_id() +
            ", SERVICE_NAME=" + getService_name() +
            ", FLOW_ID=" + getFlow_id() +
            ", DATA_ID=" + getData_id() +
            ", NAME=" + getName() +
            ", START_TIME=" + getStart_time() +
            ", END_TIME=" + getEnd_time() +
            ", APPROVAL_STATUS=" + getApproval_status() +
            ", C_TASK_ID=" + getc_task_id() +
            ", C_TASK_NAME=" + getc_task_name() +
            ", C_TINS_ID=" + getc_tins_id() +
            ", PROCESS_USER_ID=" + getProcess_user_id() +
    "}";
    }
}
