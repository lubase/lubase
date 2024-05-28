package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 表类型
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("dm_table_type")
    public class DmTableTypeEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "dm_table_type";
    public static final String COL_ID = "id";
    public static final String COL_TYPE_NAME = "type_name";
    public static final String COL_MODULE_ID = "module_id";
    public static final String COL_PRE = "pre";
    public static final String COL_VISIBLE = "visible";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";

        public String getType_name() {
            return TypeConverterUtils.object2String(super.get("type_name"));
        }

            public void setType_name(String type_name) {
        super.put("type_name",type_name);
        }
        public Long getModule_id() {
            return TypeConverterUtils.object2Long(super.get("module_id"));
        }

            public void setModule_id(Long module_id) {
        super.put("module_id",module_id);
        }
        public String getPre() {
            return TypeConverterUtils.object2String(super.get("pre"));
        }

            public void setPre(String pre) {
        super.put("pre",pre);
        }
        public Integer getVisible() {
            return TypeConverterUtils.object2Integer(super.get("visible"));
        }

            public void setVisible(Integer visible) {
        super.put("visible",visible);
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
    return "DmTableTypeEntity{" +
            "ID=" + getId() +
            ", TYPE_NAME=" + getType_name() +
            ", MODULE_ID=" + getModule_id() +
            ", PRE=" + getPre() +
            ", VISIBLE=" + getVisible() +
            ", ORDER_ID=" + getOrder_id() +
            ", DELETE_TAG=" + getDelete_tag() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
    "}";
    }
}
