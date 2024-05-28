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
    @TableName("wf_tdata")
    public class WfTDataEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "wf_tdata";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FINS_ID = "fins_id";
    public static final String COL_TINS_ID = "tins_id";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_TASK_ID = "task_id";
    public static final String COL_JSON_DATA = "json_data";

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
        public String getTins_id() {
            return TypeConverterUtils.object2String(super.get("tins_id"));
        }

            public void setTins_id(String tins_id) {
        super.put("tins_id",tins_id);
        }
        public String getFlow_id() {
            return TypeConverterUtils.object2String(super.get("flow_id"));
        }

            public void setFlow_id(String flow_id) {
        super.put("flow_id",flow_id);
        }
        public String getTask_id() {
            return TypeConverterUtils.object2String(super.get("task_id"));
        }

            public void setTask_id(String task_id) {
        super.put("task_id",task_id);
        }
        public String getJson_data() {
            return TypeConverterUtils.object2String(super.get("json_data"));
        }

            public void setJson_data(String json_data) {
        super.put("json_data",json_data);
        }

    @Override
    public String toString() {
    return "WfTDataEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", FINS_ID=" + getFins_id() +
            ", TINS_ID=" + getTins_id() +
            ", FLOW_ID=" + getFlow_id() +
            ", TASK_ID=" + getTask_id() +
            ", JSON_DATA=" + getJson_data() +
    "}";
    }
}
