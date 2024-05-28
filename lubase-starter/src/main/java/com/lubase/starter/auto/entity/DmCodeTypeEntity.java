package com.lubase.starter.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 代码表类型
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("dm_code_type")
    public class DmCodeTypeEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "dm_code_type";
    public static final String COL_ID = "id";
    public static final String COL_TYPE_NAME = "type_name";
    public static final String COL_CATEGORY = "category";
    public static final String COL_PYDM = "pydm";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_CLIENT_USE = "client_use";
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
        public Long getCategory() {
            return TypeConverterUtils.object2Long(super.get("category"));
        }

            public void setCategory(Long category) {
        super.put("category",category);
        }
        public String getPydm() {
            return TypeConverterUtils.object2String(super.get("pydm"));
        }

            public void setPydm(String pydm) {
        super.put("pydm",pydm);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }
        public Boolean getClient_use() {
            return TypeConverterUtils.object2Boolean(super.get("client_use"));
        }

            public void setClient_use(Boolean client_use) {
        super.put("client_use",client_use);
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
    return "DmCodeTypeEntity{" +
            "ID=" + getId() +
            ", TYPE_NAME=" + getType_name() +
            ", CATEGORY=" + getCategory() +
            ", PYDM=" + getPydm() +
            ", ORDER_ID=" + getOrder_id() +
            ", CLIENT_USE=" + getClient_use() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
    "}";
    }
}
