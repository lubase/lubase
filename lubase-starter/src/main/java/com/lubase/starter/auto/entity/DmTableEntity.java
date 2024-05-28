package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 表注册信息
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("dm_table")
    public class DmTableEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "dm_table";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_CODE = "table_code";
    public static final String COL_TABLE_TYPE_ID = "table_type_id";
    public static final String COL_TABLE_NAME = "table_name";
    public static final String COL_VISIBLE = "visible";
    public static final String COL_IS_LOG = "is_log";
    public static final String COL_IS_ADD = "is_add";
    public static final String COL_IS_EDIT = "is_edit";
    public static final String COL_IS_DEL = "is_del";
    public static final String COL_COL_EDIT = "col_edit";
    public static final String COL_DATABASE_ID = "database_id";
    public static final String COL_FILTER = "filter";
    public static final String COL_IS_VIEW = "is_view";
    public static final String COL_FIX_FIELD = "fix_field";
    public static final String COL_FOREIGN_KEY = "foreign_key";
    public static final String COL_LOGIC_KEY = "logic_key";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";

        public String getTable_code() {
            return TypeConverterUtils.object2String(super.get("table_code"));
        }

            public void setTable_code(String table_code) {
        super.put("table_code",table_code);
        }
        public Long getTable_type_id() {
            return TypeConverterUtils.object2Long(super.get("table_type_id"));
        }

            public void setTable_type_id(Long table_type_id) {
        super.put("table_type_id",table_type_id);
        }
        public String getTable_name() {
            return TypeConverterUtils.object2String(super.get("table_name"));
        }

            public void setTable_name(String table_name) {
        super.put("table_name",table_name);
        }
        public Boolean getVisible() {
            return TypeConverterUtils.object2Boolean(super.get("visible"));
        }

            public void setVisible(Boolean visible) {
        super.put("visible",visible);
        }
        public Boolean getIs_log() {
            return TypeConverterUtils.object2Boolean(super.get("is_log"));
        }

            public void setIs_log(Boolean is_log) {
        super.put("is_log",is_log);
        }
        public Boolean getIs_add() {
            return TypeConverterUtils.object2Boolean(super.get("is_add"));
        }

            public void setIs_add(Boolean is_add) {
        super.put("is_add",is_add);
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
        public Boolean getCol_edit() {
            return TypeConverterUtils.object2Boolean(super.get("col_edit"));
        }

            public void setCol_edit(Boolean col_edit) {
        super.put("col_edit",col_edit);
        }
        public Long getDatabase_id() {
            return TypeConverterUtils.object2Long(super.get("database_id"));
        }

            public void setDatabase_id(Long database_id) {
        super.put("database_id",database_id);
        }
        public String getFilter() {
            return TypeConverterUtils.object2String(super.get("filter"));
        }

            public void setFilter(String filter) {
        super.put("filter",filter);
        }
        public Boolean getIs_view() {
            return TypeConverterUtils.object2Boolean(super.get("is_view"));
        }

            public void setIs_view(Boolean is_view) {
        super.put("is_view",is_view);
        }
        public String getFix_field() {
            return TypeConverterUtils.object2String(super.get("fix_field"));
        }

            public void setFix_field(String fix_field) {
        super.put("fix_field",fix_field);
        }
        public String getForeign_key() {
            return TypeConverterUtils.object2String(super.get("foreign_key"));
        }

            public void setForeign_key(String foreign_key) {
        super.put("foreign_key",foreign_key);
        }
        public String getLogic_key() {
            return TypeConverterUtils.object2String(super.get("logic_key"));
        }

            public void setLogic_key(String logic_key) {
        super.put("logic_key",logic_key);
        }
        public Boolean getDelete_tag() {
            return TypeConverterUtils.object2Boolean(super.get("delete_tag"));
        }

            public void setDelete_tag(Boolean delete_tag) {
        super.put("delete_tag",delete_tag);
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

    @Override
    public String toString() {
    return "DmTableEntity{" +
            "ID=" + getId() +
            ", TABLE_CODE=" + getTable_code() +
            ", TABLE_TYPE_ID=" + getTable_type_id() +
            ", TABLE_NAME=" + getTable_name() +
            ", VISIBLE=" + getVisible() +
            ", IS_LOG=" + getIs_log() +
            ", IS_ADD=" + getIs_add() +
            ", IS_EDIT=" + getIs_edit() +
            ", IS_DEL=" + getIs_del() +
            ", COL_EDIT=" + getCol_edit() +
            ", DATABASE_ID=" + getDatabase_id() +
            ", FILTER=" + getFilter() +
            ", IS_VIEW=" + getIs_view() +
            ", FIX_FIELD=" + getFix_field() +
            ", FOREIGN_KEY=" + getForeign_key() +
            ", LOGIC_KEY=" + getLogic_key() +
            ", DELETE_TAG=" + getDelete_tag() +
            ", ORDER_ID=" + getOrder_id() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
    "}";
    }
}
