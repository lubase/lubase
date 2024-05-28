package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 表单触发器
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("ss_form_trigger")
    public class SsFormTriggerEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "ss_form_trigger";
    public static final String COL_ID = "id";
    public static final String COL_FORM_TRIGGER_NAME = "form_trigger_name";
    public static final String COL_PATH = "path";
    public static final String COL_IS_RIGHT = "is_right";
    public static final String COL_BASE_INTERFACE = "base_interface";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_TABLE_CODE = "table_code";

    public String getForm_trigger_name() {
        return TypeConverterUtils.object2String(super.get("form_trigger_name"));
    }

    public void setForm_trigger_name(String form_trigger_name) {
        super.put("form_trigger_name",form_trigger_name);
    }
    public String getPath() {
        return TypeConverterUtils.object2String(super.get("path"));
    }

    public void setPath(String path) {
        super.put("path",path);
    }
    public Boolean getIs_right() {
        return TypeConverterUtils.object2Boolean(super.get("is_right"));
    }

    public void setIs_right(Boolean is_right) {
        super.put("is_right",is_right);
    }
    public String getBase_interface() {
        return TypeConverterUtils.object2String(super.get("base_interface"));
    }

    public void setBase_interface(String base_interface) {
        super.put("base_interface",base_interface);
    }
    public Integer getOrder_id() {
        return TypeConverterUtils.object2Integer(super.get("order_id"));
    }

    public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
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
    public String getTable_code() {
        return TypeConverterUtils.object2String(super.get("table_code"));
    }

    public void setTable_code(String table_code) {
        super.put("table_code",table_code);
    }

    @Override
    public String toString() {
        return "SsFormTriggerEntity2{" +
                "ID=" + getId() +
                ", FORM_TRIGGER_NAME=" + getForm_trigger_name() +
                ", PATH=" + getPath() +
                ", IS_RIGHT=" + getIs_right() +
                ", BASE_INTERFACE=" + getBase_interface() +
                ", ORDER_ID=" + getOrder_id() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", TABLE_CODE=" + getTable_code() +
                "}";
    }
}
