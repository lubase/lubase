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
    @TableName("wf_oper")
    public class WfOperEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "wf_oper";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_TASK_ID = "task_id";
    public static final String COL_OPER_TYPE = "oper_type";
    public static final String COL_OPER_VALUE = "oper_value";
    public static final String COL_OPER_DESC = "oper_desc";
    public static final String COL_ORDER_ID = "order_id";

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
        public String getOper_type() {
            return TypeConverterUtils.object2String(super.get("oper_type"));
        }

            public void setOper_type(String oper_type) {
        super.put("oper_type",oper_type);
        }
        public String getOper_value() {
            return TypeConverterUtils.object2String(super.get("oper_value"));
        }

            public void setOper_value(String oper_value) {
        super.put("oper_value",oper_value);
        }
        public String getOper_desc() {
            return TypeConverterUtils.object2String(super.get("oper_desc"));
        }

            public void setOper_desc(String oper_desc) {
        super.put("oper_desc",oper_desc);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }

    @Override
    public String toString() {
    return "WfOperEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", TASK_ID=" + getTask_id() +
            ", OPER_TYPE=" + getOper_type() +
            ", OPER_VALUE=" + getOper_value() +
            ", OPER_DESC=" + getOper_desc() +
            ", ORDER_ID=" + getOrder_id() +
    "}";
    }
}
