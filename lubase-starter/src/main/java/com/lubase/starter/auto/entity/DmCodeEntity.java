package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 代码表管理
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("dm_code")
    public class DmCodeEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "dm_code";
    public static final String COL_ID = "id";
    public static final String COL_CODE_TYPE_ID = "code_type_id";
    public static final String COL_CODE_VALUE = "code_value";
    public static final String COL_CODE_NAME = "code_name";
    public static final String COL_PYDM = "pydm";
    public static final String COL_PARENT_CODE = "parent_code";
    public static final String COL_ENABLE_TAG = "enable_tag";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";
    public static final String COL_ORDER_ID = "order_id";

        public Long getCode_type_id() {
            return TypeConverterUtils.object2Long(super.get("code_type_id"));
        }

            public void setCode_type_id(Long code_type_id) {
        super.put("code_type_id",code_type_id);
        }
        public String getCode_value() {
            return TypeConverterUtils.object2String(super.get("code_value"));
        }

            public void setCode_value(String code_value) {
        super.put("code_value",code_value);
        }
        public String getCode_name() {
            return TypeConverterUtils.object2String(super.get("code_name"));
        }

            public void setCode_name(String code_name) {
        super.put("code_name",code_name);
        }
        public String getPydm() {
            return TypeConverterUtils.object2String(super.get("pydm"));
        }

            public void setPydm(String pydm) {
        super.put("pydm",pydm);
        }
        public String getParent_code() {
            return TypeConverterUtils.object2String(super.get("parent_code"));
        }

            public void setParent_code(String parent_code) {
        super.put("parent_code",parent_code);
        }
        public Boolean getEnable_tag() {
            return TypeConverterUtils.object2Boolean(super.get("enable_tag"));
        }

            public void setEnable_tag(Boolean enable_tag) {
        super.put("enable_tag",enable_tag);
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
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }

    @Override
    public String toString() {
    return "DmCodeEntity{" +
            "ID=" + getId() +
            ", CODE_TYPE_ID=" + getCode_type_id() +
            ", CODE_VALUE=" + getCode_value() +
            ", CODE_NAME=" + getCode_name() +
            ", PYDM=" + getPydm() +
            ", PARENT_CODE=" + getParent_code() +
            ", ENABLE_TAG=" + getEnable_tag() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
            ", ORDER_ID=" + getOrder_id() +
    "}";
    }
}
