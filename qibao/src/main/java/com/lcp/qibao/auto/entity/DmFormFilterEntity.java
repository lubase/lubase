package com.lcp.qibao.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 
    * </p>
*
* @author A
* @since 2022-08-05
*/
    @TableName("dm_form_filter")
    public class DmFormFilterEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "dm_form_filter";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FORM_ID = "form_id";
    public static final String COL_COLUMN_ID = "column_id";
    public static final String COL_RELY_COLUMN = "rely_column";
    public static final String COL_MEMO = "memo";
    public static final String COL_QUERY_SERVICE = "query_service";
    public static final String COL_GRID_QUERY = "grid_query";
    public static final String COL_GRID_INFO = "grid_info";
    public static final String COL_SEARCH_FILTER = "search_filter";

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
    public Long getForm_id() {
        return TypeConverterUtils.object2Long(super.get("form_id"));
    }

    public void setForm_id(Long form_id) {
        super.put("form_id",form_id);
    }
    public Long getColumn_id() {
        return TypeConverterUtils.object2Long(super.get("column_id"));
    }

    public void setColumn_id(Long column_id) {
        super.put("column_id",column_id);
    }
    public String getRely_column() {
        return TypeConverterUtils.object2String(super.get("rely_column"));
    }

    public void setRely_column(String rely_column) {
        super.put("rely_column",rely_column);
    }
    public String getMemo() {
        return TypeConverterUtils.object2String(super.get("memo"));
    }

    public void setMemo(String memo) {
        super.put("memo",memo);
    }
    public String getQuery_service() {
        return TypeConverterUtils.object2String(super.get("query_service"));
    }

    public void setQuery_service(String query_service) {
        super.put("query_service",query_service);
    }
    public String getGrid_query() {
        return TypeConverterUtils.object2String(super.get("grid_query"));
    }

    public void setGrid_query(String grid_query) {
        super.put("grid_query",grid_query);
    }
    public String getGrid_info() {
        return TypeConverterUtils.object2String(super.get("grid_info"));
    }

    public void setGrid_info(String grid_info) {
        super.put("grid_info",grid_info);
    }
    public String getSearch_filter() {
        return TypeConverterUtils.object2String(super.get("search_filter"));
    }

    public void setSearch_filter(String search_filter) {
        super.put("search_filter",search_filter);
    }

    @Override
    public String toString() {
        return "DmFormFilter2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", FORM_ID=" + getForm_id() +
                ", COLUMN_ID=" + getColumn_id() +
                ", RELY_COLUMN=" + getRely_column() +
                ", MEMO=" + getMemo() +
                ", QUERY_SERVICE=" + getQuery_service() +
                ", GRID_QUERY=" + getGrid_query() +
                ", GRID_INFO=" + getGrid_info() +
                ", SEARCH_FILTER=" + getSearch_filter() +
                "}";
    }
}
