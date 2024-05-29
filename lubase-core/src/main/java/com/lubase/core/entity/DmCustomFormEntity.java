package com.lubase.core.entity;

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
* @author A
* @since 2022-08-16
*/
    @TableName("dm_custom_form")
    public class DmCustomFormEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "dm_custom_form";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_ID = "table_id";
    public static final String COL_TABLE_CODE = "table_code";
    public static final String COL_FORM_NAME = "form_name";
    public static final String COL_FORM_TYPE = "form_type";
    public static final String COL_DATA = "data";
    public static final String COL_COLS = "cols";
    public static final String COL_CHILD_TABLE = "child_table";
    public static final String COL_EXTEND_SCRIPT = "extend_script";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_DATABASE_ID = "database_id";
    public static final String COL_TRIGGER_ID = "trigger_id";
    public static final String COL_TEST_PRIMARY = "test_primary";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FORM_CONFIG = "form_config";
    public static final String COL_TRIGGER_PATH = "trigger_path";

    public Long getTable_id() {
        return TypeConverterUtils.object2Long(super.get("table_id"));
    }

    public void setTable_id(Long table_id) {
        super.put("table_id",table_id);
    }
    public String getTable_code() {
        return TypeConverterUtils.object2String(super.get("table_code"));
    }

    public void setTable_code(String table_code) {
        super.put("table_code",table_code);
    }
    public String getForm_name() {
        return TypeConverterUtils.object2String(super.get("form_name"));
    }

    public void setForm_name(String form_name) {
        super.put("form_name",form_name);
    }
    public String getForm_type() {
        return TypeConverterUtils.object2String(super.get("form_type"));
    }

    public void setForm_type(String form_type) {
        super.put("form_type",form_type);
    }
    public String getData() {
        return TypeConverterUtils.object2String(super.get("data"));
    }

    public void setData(String data) {
        super.put("data",data);
    }
    public String getCols() {
        return TypeConverterUtils.object2String(super.get("cols"));
    }

    public void setCols(String cols) {
        super.put("cols",cols);
    }
    public String getChild_table() {
        return TypeConverterUtils.object2String(super.get("child_table"));
    }

    public void setChild_table(String child_table) {
        super.put("child_table",child_table);
    }
    public String getExtend_script() {
        return TypeConverterUtils.object2String(super.get("extend_script"));
    }

    public void setExtend_script(String extend_script) {
        super.put("extend_script",extend_script);
    }
    public Integer getOrder_id() {
        return TypeConverterUtils.object2Integer(super.get("order_id"));
    }

    public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
    }
    public String getDatabase_id() {
        return TypeConverterUtils.object2String(super.get("database_id"));
    }

    public void setDatabase_id(Long database_id) {
        super.put("database_id",database_id);
    }
    public Long getTrigger_id() {
        return TypeConverterUtils.object2Long(super.get("trigger_id"));
    }

    public void setTrigger_id(Long trigger_id) {
        super.put("trigger_id",trigger_id);
    }
    public Long getTest_primary() {
        return TypeConverterUtils.object2Long(super.get("test_primary"));
    }

    public void setTest_primary(Long test_primary) {
        super.put("test_primary",test_primary);
    }
    public Boolean getDelete_tag() {
        return TypeConverterUtils.object2Boolean(super.get("delete_tag"));
    }

    public void setDelete_tag(Boolean delete_tag) {
        super.put("delete_tag",delete_tag);
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
    public String getForm_config() {
        return TypeConverterUtils.object2String(super.get("form_config"));
    }

    public void setForm_config(String form_config) {
        super.put("form_config",form_config);
    }
    public String getTrigger_path() {
        return TypeConverterUtils.object2String(super.get("trigger_path"));
    }

    public void setTrigger_path(String trigger_path) {
        super.put("trigger_path",trigger_path);
    }

    @Override
    public String toString() {
        return "DmCustomFormEntity2{" +
                "ID=" + getId() +
                ", TABLE_ID=" + getTable_id() +
                ", TABLE_CODE=" + getTable_code() +
                ", FORM_NAME=" + getForm_name() +
                ", FORM_TYPE=" + getForm_type() +
                ", DATA=" + getData() +
                ", COLS=" + getCols() +
                ", CHILD_TABLE=" + getChild_table() +
                ", EXTEND_SCRIPT=" + getExtend_script() +
                ", ORDER_ID=" + getOrder_id() +
                ", DATABASE_ID=" + getDatabase_id() +
                ", TRIGGER_ID=" + getTrigger_id() +
                ", TEST_PRIMARY=" + getTest_primary() +
                ", DELETE_TAG=" + getDelete_tag() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", FORM_CONFIG=" + getForm_config() +
                ", TRIGGER_PATH=" + getTrigger_path() +
                "}";
    }
}
