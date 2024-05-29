package com.lubase.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.orm.util.TypeConverterUtils;
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
    @TableName("dm_column")
    public class DmColumnEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "dm_column";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_ID = "table_id";
    public static final String COL_TABLE_CODE = "table_code";
    public static final String COL_COL_CODE = "col_code";
    public static final String COL_COL_NAME = "col_name";
    public static final String COL_DATA_TYPE = "data_type";
    public static final String COL_LENGTH = "length";
    public static final String COL_ALLOW_NULL = "allow_null";
    public static final String COL_COL_DEFAULT = "col_default";
    public static final String COL_DATA_FORMAT = "data_format";
    public static final String COL_ELE_TYPE = "ele_type";
    public static final String COL_ELE_DISTYPE = "ele_distype";
    public static final String COL_IS_MULTIVALUED = "is_multivalued";
    public static final String COL_VISIBLE = "visible";
    public static final String COL_CODE_TYPE_ID = "code_type_id";
    public static final String COL_LOOKUP = "lookup";
    public static final String COL_GROUP_NAME = "group_name";
    public static final String COL_INPUT_DESC = "input_desc";
    public static final String COL_TABLEFILTER_SQLMODE = "tablefilter_sqlmode";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_ROW_SPAN = "row_span";
    public static final String COL_COL_SPAN = "col_span";
    public static final String COL_VALIDATE_EXPRESSION = "validate_expression";
    public static final String COL_VALIDATE_MSG = "validate_msg";
    public static final String COL_NEED_LOG = "need_log";
    public static final String COL_DISPLAY_WIDTH = "display_width";
    public static final String COL_RENDER_FLAG = "render_flag";

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
        public String getData_type() {
            return TypeConverterUtils.object2String(super.get("data_type"));
        }

            public void setData_type(String data_type) {
        super.put("data_type",data_type);
        }
        public Integer getLength() {
            return TypeConverterUtils.object2Integer(super.get("length"));
        }

            public void setLength(Integer length) {
        super.put("length",length);
        }
        public Boolean getAllow_null() {
            return TypeConverterUtils.object2Boolean(super.get("allow_null"));
        }

            public void setAllow_null(Boolean allow_null) {
        super.put("allow_null",allow_null);
        }
        public String getCol_default() {
            return TypeConverterUtils.object2String(super.get("col_default"));
        }

            public void setCol_default(String col_default) {
        super.put("col_default",col_default);
        }
        public String getData_format() {
            return TypeConverterUtils.object2String(super.get("data_format"));
        }

            public void setData_format(String data_format) {
        super.put("data_format",data_format);
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
        public Integer getIs_multivalued() {
            return TypeConverterUtils.object2Integer(super.get("is_multivalued"));
        }

            public void setIs_multivalued(Integer is_multivalued) {
        super.put("is_multivalued",is_multivalued);
        }
        public Integer getVisible() {
            return TypeConverterUtils.object2Integer(super.get("visible"));
        }

            public void setVisible(Integer visible) {
        super.put("visible",visible);
        }
        public Long getCode_type_id() {
            return TypeConverterUtils.object2Long(super.get("code_type_id"));
        }

            public void setCode_type_id(Long code_type_id) {
        super.put("code_type_id",code_type_id);
        }
        public String getLookup() {
            return TypeConverterUtils.object2String(super.get("lookup"));
        }

            public void setLookup(String lookup) {
        super.put("lookup",lookup);
        }
        public String getGroup_name() {
            return TypeConverterUtils.object2String(super.get("group_name"));
        }

            public void setGroup_name(String group_name) {
        super.put("group_name",group_name);
        }
        public String getInput_desc() {
            return TypeConverterUtils.object2String(super.get("input_desc"));
        }

            public void setInput_desc(String input_desc) {
        super.put("input_desc",input_desc);
        }
        public Integer getTablefilter_sqlmode() {
            return TypeConverterUtils.object2Integer(super.get("tablefilter_sqlmode"));
        }

            public void setTablefilter_sqlmode(Integer tablefilter_sqlmode) {
        super.put("tablefilter_sqlmode",tablefilter_sqlmode);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }
        public Integer getRow_span() {
            return TypeConverterUtils.object2Integer(super.get("row_span"));
        }

            public void setRow_span(Integer row_span) {
        super.put("row_span",row_span);
        }
        public Integer getCol_span() {
            return TypeConverterUtils.object2Integer(super.get("col_span"));
        }

            public void setCol_span(Integer col_span) {
        super.put("col_span",col_span);
        }
        public String getValidate_expression() {
            return TypeConverterUtils.object2String(super.get("validate_expression"));
        }

            public void setValidate_expression(String validate_expression) {
        super.put("validate_expression",validate_expression);
        }
        public String getValidate_msg() {
            return TypeConverterUtils.object2String(super.get("validate_msg"));
        }

            public void setValidate_msg(String validate_msg) {
        super.put("validate_msg",validate_msg);
        }
        public Integer getNeed_log() {
            return TypeConverterUtils.object2Integer(super.get("need_log"));
        }

            public void setNeed_log(Integer need_log) {
        super.put("need_log",need_log);
        }
        public Integer getDisplay_width() {
            return TypeConverterUtils.object2Integer(super.get("display_width"));
        }

            public void setDisplay_width(Integer display_width) {
        super.put("display_width",display_width);
        }
        public Boolean getRender_flag() {
            return TypeConverterUtils.object2Boolean(super.get("render_flag"));
        }

            public void setRender_flag(Boolean render_flag) {
        super.put("render_flag",render_flag);
        }

    @Override
    public String toString() {
    return "DmColumnEntity{" +
            "ID=" + getId() +
            ", TABLE_ID=" + getTable_id() +
            ", TABLE_CODE=" + getTable_code() +
            ", COL_CODE=" + getCol_code() +
            ", COL_NAME=" + getCol_name() +
            ", DATA_TYPE=" + getData_type() +
            ", LENGTH=" + getLength() +
            ", ALLOW_NULL=" + getAllow_null() +
            ", COL_DEFAULT=" + getCol_default() +
            ", DATA_FORMAT=" + getData_format() +
            ", ELE_TYPE=" + getEle_type() +
            ", ELE_DISTYPE=" + getEle_distype() +
            ", IS_MULTIVALUED=" + getIs_multivalued() +
            ", VISIBLE=" + getVisible() +
            ", CODE_TYPE_ID=" + getCode_type_id() +
            ", LOOKUP=" + getLookup() +
            ", GROUP_NAME=" + getGroup_name() +
            ", INPUT_DESC=" + getInput_desc() +
            ", TABLEFILTER_SQLMODE=" + getTablefilter_sqlmode() +
            ", ORDER_ID=" + getOrder_id() +
            ", ROW_SPAN=" + getRow_span() +
            ", COL_SPAN=" + getCol_span() +
            ", VALIDATE_EXPRESSION=" + getValidate_expression() +
            ", VALIDATE_MSG=" + getValidate_msg() +
            ", NEED_LOG=" + getNeed_log() +
            ", DISPLAY_WIDTH=" + getDisplay_width() +
            ", RENDER_FLAG=" + getRender_flag() +
    "}";
    }
}
