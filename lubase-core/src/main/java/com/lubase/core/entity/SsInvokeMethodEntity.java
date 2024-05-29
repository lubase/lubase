package com.lubase.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 注册方法
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("ss_invoke_method")
    public class SsInvokeMethodEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "ss_invoke_method";
    public static final String COL_ID = "id";
    public static final String COL_METHOD_NAME = "method_name";
    public static final String COL_PATH = "path";
    public static final String COL_IS_RIGHT = "is_right";
    public static final String COL_BASE_INTERFACE = "base_interface";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";

        public String getMethod_name() {
            return TypeConverterUtils.object2String(super.get("method_name"));
        }

            public void setMethod_name(String method_name) {
        super.put("method_name",method_name);
        }
        public String getPath() {
            return TypeConverterUtils.object2String(super.get("path"));
        }

            public void setPath(String path) {
        super.put("path",path);
        }
        public Boolean getIs_right() {
            return TypeConverterUtils.object2Boolean(super.get("is_right"));
        }

            public void setIs_right(Boolean is_right) {
        super.put("is_right",is_right);
        }
        public String getBase_interface() {
            return TypeConverterUtils.object2String(super.get("base_interface"));
        }

            public void setBase_interface(String base_interface) {
        super.put("base_interface",base_interface);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
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
    return "SsInvokeMethodEntity{" +
            "ID=" + getId() +
            ", METHOD_NAME=" + getMethod_name() +
            ", PATH=" + getPath() +
            ", IS_RIGHT=" + getIs_right() +
            ", BASE_INTERFACE=" + getBase_interface() +
            ", ORDER_ID=" + getOrder_id() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
    "}";
    }
}
