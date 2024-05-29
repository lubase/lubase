package com.lubase.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 
    * </p>
*
* @author A
* @since 2022-07-05
*/
    @TableName("sa_role")
    public class SaRoleEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "sa_role";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_ENABLE_TAG = "enable_tag";
    public static final String COL_SIGNED = "signed";
    public static final String COL_ROLE_NAME = "role_name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PARENT_ID = "parent_id";

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
        public Boolean getEnable_tag() {
            return TypeConverterUtils.object2Boolean(super.get("enable_tag"));
        }

            public void setEnable_tag(Boolean enable_tag) {
        super.put("enable_tag",enable_tag);
        }
        public Integer getSigned() {
            return TypeConverterUtils.object2Integer(super.get("signed"));
        }

            public void setSigned(Integer signed) {
        super.put("signed",signed);
        }
        public String getRole_name() {
            return TypeConverterUtils.object2String(super.get("role_name"));
        }

            public void setRole_name(String role_name) {
        super.put("role_name",role_name);
        }
        public String getDescription() {
            return TypeConverterUtils.object2String(super.get("description"));
        }

            public void setDescription(String description) {
        super.put("description",description);
        }
        public Long getParent_id() {
            return TypeConverterUtils.object2Long(super.get("parent_id"));
        }

            public void setParent_id(Long parent_id) {
        super.put("parent_id",parent_id);
        }

    @Override
    public String toString() {
    return "SaRoleEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", ORDER_ID=" + getOrder_id() +
            ", DELETE_TAG=" + getDelete_tag() +
            ", ENABLE_TAG=" + getEnable_tag() +
            ", SIGNED=" + getSigned() +
            ", ROLE_NAME=" + getRole_name() +
            ", DESCRIPTION=" + getDescription() +
            ", PARENT_ID=" + getParent_id() +
    "}";
    }
}