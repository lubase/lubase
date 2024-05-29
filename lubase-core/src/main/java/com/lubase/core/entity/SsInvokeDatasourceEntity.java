package com.lubase.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 动态数据源
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("ss_invoke_datasource")
    public class SsInvokeDatasourceEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "ss_invoke_datasource";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_CODE = "table_code";
    public static final String COL_QUERY_SETTING = "query_setting";
    public static final String COL_PARAM_COUNT = "param_count";
    public static final String COL_QUERY_SETTING_TYPE = "query_setting_type";
    public static final String COL_RESPONSE_TYPE = "response_type";
    public static final String COL_SQL_SETTING = "sql_setting";
    public static final String COL_DATASOURCE_NAME = "datasource_name";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_DELETE_TAG = "delete_tag";
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
        public String getQuery_setting() {
            return TypeConverterUtils.object2String(super.get("query_setting"));
        }

            public void setQuery_setting(String query_setting) {
        super.put("query_setting",query_setting);
        }
        public Integer getParam_count() {
            return TypeConverterUtils.object2Integer(super.get("param_count"));
        }

            public void setParam_count(Integer param_count) {
        super.put("param_count",param_count);
        }
        public Integer getQuery_setting_type() {
            return TypeConverterUtils.object2Integer(super.get("query_setting_type"));
        }

            public void setQuery_setting_type(Integer query_setting_type) {
        super.put("query_setting_type",query_setting_type);
        }
        public Integer getResponse_type() {
            return TypeConverterUtils.object2Integer(super.get("response_type"));
        }

            public void setResponse_type(Integer response_type) {
        super.put("response_type",response_type);
        }
        public String getSql_setting() {
            return TypeConverterUtils.object2String(super.get("sql_setting"));
        }

            public void setSql_setting(String sql_setting) {
        super.put("sql_setting",sql_setting);
        }
        public String getDatasource_name() {
            return TypeConverterUtils.object2String(super.get("datasource_name"));
        }

            public void setDatasource_name(String datasource_name) {
        super.put("datasource_name",datasource_name);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }
        public Boolean getDelete_tag() {
            return TypeConverterUtils.object2Boolean(super.get("delete_tag"));
        }

            public void setDelete_tag(Boolean delete_tag) {
        super.put("delete_tag",delete_tag);
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
    return "SsInvokeDatasourceEntity{" +
            "ID=" + getId() +
            ", TABLE_CODE=" + getTable_code() +
            ", QUERY_SETTING=" + getQuery_setting() +
            ", PARAM_COUNT=" + getParam_count() +
            ", QUERY_SETTING_TYPE=" + getQuery_setting_type() +
            ", RESPONSE_TYPE=" + getResponse_type() +
            ", SQL_SETTING=" + getSql_setting() +
            ", DATASOURCE_NAME=" + getDatasource_name() +
            ", ORDER_ID=" + getOrder_id() +
            ", DELETE_TAG=" + getDelete_tag() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
    "}";
    }
}
