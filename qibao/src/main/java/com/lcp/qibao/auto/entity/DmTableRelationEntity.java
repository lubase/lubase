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
* @since 2022-08-07
*/
    @TableName("dm_table_relation")
    public class DmTableRelationEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "dm_table_relation";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_APP_ID = "app_id";
    public static final String COL_MAIN_TABLE_ID = "main_table_id";
    public static final String COL_CHILD_TABLE_ID = "child_table_id";
    public static final String COL_FK_COLUMN_ID = "fk_column_id";
    public static final String COL_FK_COLUMN_CODE = "fk_column_code";

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
    public Long getApp_id() {
        return TypeConverterUtils.object2Long(super.get("app_id"));
    }

    public void setApp_id(Long app_id) {
        super.put("app_id",app_id);
    }
    public Long getMain_table_id() {
        return TypeConverterUtils.object2Long(super.get("main_table_id"));
    }

    public void setMain_table_id(Long main_table_id) {
        super.put("main_table_id",main_table_id);
    }
    public Long getChild_table_id() {
        return TypeConverterUtils.object2Long(super.get("child_table_id"));
    }

    public void setChild_table_id(Long child_table_id) {
        super.put("child_table_id",child_table_id);
    }
    public Long getFk_column_id() {
        return TypeConverterUtils.object2Long(super.get("fk_column_id"));
    }

    public void setFk_column_id(Long fk_column_id) {
        super.put("fk_column_id",fk_column_id);
    }
    public String getFk_column_code() {
        return TypeConverterUtils.object2String(super.get("fk_column_code"));
    }

    public void setFk_column_code(String fk_column_code) {
        super.put("fk_column_code",fk_column_code);
    }

    @Override
    public String toString() {
        return "DmTableRelationEntity2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", APP_ID=" + getApp_id() +
                ", MAIN_TABLE_ID=" + getMain_table_id() +
                ", CHILD_TABLE_ID=" + getChild_table_id() +
                ", FK_COLUMN_ID=" + getFk_column_id() +
                ", FK_COLUMN_CODE=" + getFk_column_code() +
                "}";
    }
}
