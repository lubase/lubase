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
* @author caogengmeng
* @since 2022-08-11
*/
    @TableName("sd_personalization")
    public class SdPersonalizationEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "sd_personalization";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_ACCOUNT_ID = "account_id";
    public static final String COL_PAGE_ID = "page_id";
    public static final String COL_DISPLAY_LIST = "display_list";
    public static final String COL_LOCK_COLUMN_COUNT = "lock_column_count";
    public static final String COL_COLUMN_WIDTH_SETTING = "column_width_setting";

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
    public Long getAccount_id() {
        return TypeConverterUtils.object2Long(super.get("account_id"));
    }

    public void setAccount_id(Long account_id) {
        super.put("account_id",account_id);
    }
    public Long getPage_id() {
        return TypeConverterUtils.object2Long(super.get("page_id"));
    }

    public void setPage_id(Long page_id) {
        super.put("page_id",page_id);
    }
    public String getDisplay_list() {
        return TypeConverterUtils.object2String(super.get("display_list"));
    }

    public void setDisplay_list(String display_list) {
        super.put("display_list",display_list);
    }
    public Integer getLock_column_count() {
        return TypeConverterUtils.object2Integer(super.get("lock_column_count"));
    }

    public void setLock_column_count(Integer lock_column_count) {
        super.put("lock_column_count",lock_column_count);
    }
    public String getColumn_width_setting() {
        return TypeConverterUtils.object2String(super.get("column_width_setting"));
    }

    public void setColumn_width_setting(String column_width_setting) {
        super.put("column_width_setting",column_width_setting);
    }

    @Override
    public String toString() {
        return "sd_personalization2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", ACCOUNT_ID=" + getAccount_id() +
                ", PAGE_ID=" + getPage_id() +
                ", DISPLAY_LIST=" + getDisplay_list() +
                ", LOCK_COLUMN_COUNT=" + getLock_column_count() +
                ", COLUMN_WIDTH_SETTING=" + getColumn_width_setting() +
                "}";
    }
}