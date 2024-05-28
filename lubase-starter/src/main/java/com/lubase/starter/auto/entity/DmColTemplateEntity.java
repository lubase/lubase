package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

/**
* hello custom
* <p>
    * 
    * </p>
*
* @author A
* @since 2022-08-07
*/
    @TableName("dm_col_template")
    public class DmColTemplateEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "dm_col_template";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_ID = "table_id";
    public static final String COL_ELE_TYPE = "ele_type";
    public static final String COL_ELE_DISTYPE = "ele_distype";
    public static final String COL_DATA_TYPE = "data_type";
    public static final String COL_DATA_FORMAT = "data_format";
    public static final String COL_COL_CODE = "col_code";
    public static final String COL_COL_NAME = "col_name";
    public static final String COL_ALLOW_NULL = "allow_null";
    public static final String COL_DEFAULT_FIELD = "default_field";
    public static final String COL_RENDER_FLAG = "render_flag";
    public static final String COL_COL_DEFAULT = "col_default";

        public Long getTable_id() {
            return TypeConverterUtils.object2Long(super.get("table_id"));
        }

            public void setTable_id(Long table_id) {
        super.put("table_id",table_id);
        }
        public Integer getEle_type() {
            return TypeConverterUtils.object2Integer(super.get("ele_type"));
        }

            public void setEle_type(Integer ele_type) {
        super.put("ele_type",ele_type);
        }
        public Integer getEle_distype() {
            return TypeConverterUtils.object2Integer(super.get("ele_distype"));
        }

            public void setEle_distype(Integer ele_distype) {
        super.put("ele_distype",ele_distype);
        }
        public String getData_type() {
            return TypeConverterUtils.object2String(super.get("data_type"));
        }

            public void setData_type(String data_type) {
        super.put("data_type",data_type);
        }
        public String getData_format() {
            return TypeConverterUtils.object2String(super.get("data_format"));
        }

            public void setData_format(String data_format) {
        super.put("data_format",data_format);
        }
        public String getCol_code() {
            return TypeConverterUtils.object2String(super.get("col_code"));
        }

            public void setCol_code(String col_code) {
        super.put("col_code",col_code);
        }
        public String getCol_name() {
            return TypeConverterUtils.object2String(super.get("col_name"));
        }

            public void setCol_name(String col_name) {
        super.put("col_name",col_name);
        }
        public Boolean getAllow_null() {
            return TypeConverterUtils.object2Boolean(super.get("allow_null"));
        }

            public void setAllow_null(Boolean allow_null) {
        super.put("allow_null",allow_null);
        }
        public Boolean getDefault_field() {
            return TypeConverterUtils.object2Boolean(super.get("default_field"));
        }

            public void setDefault_field(Boolean default_field) {
        super.put("default_field",default_field);
        }
        public Boolean getRender_flag() {
            return TypeConverterUtils.object2Boolean(super.get("render_flag"));
        }

            public void setRender_flag(Boolean render_flag) {
        super.put("render_flag",render_flag);
        }
        public String getCol_default() {
            return TypeConverterUtils.object2String(super.get("col_default"));
        }

            public void setCol_default(String col_default) {
        super.put("col_default",col_default);
        }

    @Override
    public String toString() {
    return "DmColTemplateEntity{" +
            "ID=" + getId() +
            ", TABLE_ID=" + getTable_id() +
            ", ELE_TYPE=" + getEle_type() +
            ", ELE_DISTYPE=" + getEle_distype() +
            ", DATA_TYPE=" + getData_type() +
            ", DATA_FORMAT=" + getData_format() +
            ", COL_CODE=" + getCol_code() +
            ", COL_NAME=" + getCol_name() +
            ", ALLOW_NULL=" + getAllow_null() +
            ", DEFAULT_FIELD=" + getDefault_field() +
            ", RENDER_FLAG=" + getRender_flag() +
            ", COL_DEFAULT=" + getCol_default() +
    "}";
    }
}
