package com.lcp.coremodel;


import com.lcp.coremodel.util.TypeConverterUtilsMirror;

/**
 * hello custom
 * <p>
 *
 * </p>
 *
 * @author A
 * @since 2022-08-27
 */
public class SsCacheEntity extends DbEntity {
    private static final long serialVersionUID = 5584605329383028422L;
    public static final String TABLE_CODE = "ss_cache";
    public static final String COL_ID = "id";
    public static final String COL_TABLE_CODE = "table_code";
    public static final String COL_UPDATE_EVENT = "update_event";
    public static final String COL_KEY_FIELD = "key_field";
    public static final String COL_CACHE_NAME = "cache_name";
    public static final String COL_CACHE_KEY_PRE = "cache_key_pre";
    public static final String COL_MEMO = "memo";

    public String getTable_code() {
        return TypeConverterUtilsMirror.object2String(super.get("table_code"));
    }

    public void setTable_code(String table_code) {
        super.put("table_code", table_code);
    }

    public Integer getUpdate_event() {
        return TypeConverterUtilsMirror.object2Integer(super.get("update_event"));
    }

    public void setUpdate_event(Integer update_event) {
        super.put("update_event", update_event);
    }

    public String getKey_field() {
        return TypeConverterUtilsMirror.object2String(super.get("key_field"));
    }

    public void setKey_field(String key_field) {
        super.put("key_field", key_field);
    }

    public String getCache_name() {
        return TypeConverterUtilsMirror.object2String(super.get("cache_name"));
    }

    public void setCache_name(String cache_name) {
        super.put("cache_name", cache_name);
    }

    public String getCache_key_pre() {
        return TypeConverterUtilsMirror.object2String(super.get("cache_key_pre"));
    }

    public void setCache_key_pre(String cache_key_pre) {
        super.put("cache_key_pre", cache_key_pre);
    }

    public String getMemo() {
        return TypeConverterUtilsMirror.object2String(super.get("memo"));
    }

    public void setMemo(String memo) {
        super.put("memo", memo);
    }

    @Override
    public String toString() {
        return "SsCacheEntity{" +
                "ID=" + getId() +
                ", TABLE_CODE=" + getTable_code() +
                ", UPDATE_EVENT=" + getUpdate_event() +
                ", KEY_FIELD=" + getKey_field() +
                ", CACHE_NAME=" + getCache_name() +
                ", CACHE_KEY_PRE=" + getCache_key_pre() +
                ", MEMO=" + getMemo() +
                "}";
    }
}
