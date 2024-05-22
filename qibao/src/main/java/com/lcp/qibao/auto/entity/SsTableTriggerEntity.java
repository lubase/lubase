package com.lcp.qibao.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 表触发器
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("ss_table_trigger")
    public class SsTableTriggerEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "ss_table_trigger";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_CODE = "table_code";
    public static final String COL_TRIGGER_NAME = "trigger_name";
    public static final String COL_IS_EDIT = "is_edit";
    public static final String COL_IS_DEL = "is_del";
    public static final String COL_TRIGGER_PATH = "trigger_path";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";
    public static final String COL_IS_ADD = "is_add";

        public String getTable_code() {
            return TypeConverterUtils.object2String(super.get("table_code"));
        }

            public void setTable_code(String table_code) {
        super.put("table_code",table_code);
        }
        public String getTrigger_name() {
            return TypeConverterUtils.object2String(super.get("trigger_name"));
        }

            public void setTrigger_name(String trigger_name) {
        super.put("trigger_name",trigger_name);
        }
        public Boolean getIs_edit() {
            return TypeConverterUtils.object2Boolean(super.get("is_edit"));
        }

            public void setIs_edit(Boolean is_edit) {
        super.put("is_edit",is_edit);
        }
        public Boolean getIs_del() {
            return TypeConverterUtils.object2Boolean(super.get("is_del"));
        }

            public void setIs_del(Boolean is_del) {
        super.put("is_del",is_del);
        }
        public String getTrigger_path() {
            return TypeConverterUtils.object2String(super.get("trigger_path"));
        }

            public void setTrigger_path(String trigger_path) {
        super.put("trigger_path",trigger_path);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }
        public Long getCreated_by() {
            return TypeConverterUtils.object2Long(super.get("created_by"));
        }

            public void setCreated_by(Long created_by) {
        super.put("created_by",created_by);
        }
        public LocalDateTime getCreated_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("created_time"));
        }
            public void setCreated_time(LocalDateTime created_time) {
        super.put("created_time",created_time);
        }
        public Long getUpdated_by() {
            return TypeConverterUtils.object2Long(super.get("updated_by"));
        }

            public void setUpdated_by(Long updated_by) {
        super.put("updated_by",updated_by);
        }
        public LocalDateTime getUpdated_time() {
            return TypeConverterUtils.object2LocalDateTime(super.get("updated_time"));
        }

            public void setUpdated_time(LocalDateTime updated_time) {
        super.put("updated_time",updated_time);
        }
        public Boolean getIs_add() {
            return TypeConverterUtils.object2Boolean(super.get("is_add"));
        }

            public void setIs_add(Boolean is_add) {
        super.put("is_add",is_add);
        }

    @Override
    public String toString() {
    return "SsTableTriggerEntity{" +
            "ID=" + getId() +
            ", TABLE_CODE=" + getTable_code() +
            ", TRIGGER_NAME=" + getTrigger_name() +
            ", IS_EDIT=" + getIs_edit() +
            ", IS_DEL=" + getIs_del() +
            ", TRIGGER_PATH=" + getTrigger_path() +
            ", ORDER_ID=" + getOrder_id() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
            ", IS_ADD=" + getIs_add() +
    "}";
    }
}
