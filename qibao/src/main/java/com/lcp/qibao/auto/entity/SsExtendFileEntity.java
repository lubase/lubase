package com.lcp.qibao.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 扩展文件管理
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("ss_extend_file")
    public class SsExtendFileEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "ss_extend_file";
    public static final String COL_ID = "id";
    public static final String COL_FILE_NAME = "file_name";
    public static final String COL_CATEGORY = "category";
    public static final String COL_FILE_TYPE = "file_type";
    public static final String COL_SCOPE = "scope";
    public static final String COL_FILE_KEY = "file_key";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";
    public static final String COL_MEMO = "memo";

        public String getFile_name() {
            return TypeConverterUtils.object2String(super.get("file_name"));
        }

            public void setFile_name(String file_name) {
        super.put("file_name",file_name);
        }
        public String getCategory() {
            return TypeConverterUtils.object2String(super.get("category"));
        }

            public void setCategory(String category) {
        super.put("category",category);
        }
        public String getFile_type() {
            return TypeConverterUtils.object2String(super.get("file_type"));
        }

            public void setFile_type(String file_type) {
        super.put("file_type",file_type);
        }
        public String getScope() {
            return TypeConverterUtils.object2String(super.get("scope"));
        }

            public void setScope(String scope) {
        super.put("scope",scope);
        }
        public String getFile_key() {
            return TypeConverterUtils.object2String(super.get("file_key"));
        }

            public void setFile_key(String file_key) {
        super.put("file_key",file_key);
        }
        public Boolean getDelete_tag() {
            return TypeConverterUtils.object2Boolean(super.get("delete_tag"));
        }

            public void setDelete_tag(Boolean delete_tag) {
        super.put("delete_tag",delete_tag);
        }
        public Integer getOrder_id() {
            return TypeConverterUtils.object2Integer(super.get("order_id"));
        }

            public void setOrder_id(Integer order_id) {
        super.put("order_id",order_id);
        }
        public String getGroup_id() {
            return TypeConverterUtils.object2String(super.get("group_id"));
        }

            public void setGroup_id(String group_id) {
        super.put("group_id",group_id);
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
        public String getMemo() {
            return TypeConverterUtils.object2String(super.get("memo"));
        }

            public void setMemo(String memo) {
        super.put("memo",memo);
        }

    @Override
    public String toString() {
    return "SsExtendFileEntity{" +
            "ID=" + getId() +
            ", FILE_NAME=" + getFile_name() +
            ", CATEGORY=" + getCategory() +
            ", FILE_TYPE=" + getFile_type() +
            ", SCOPE=" + getScope() +
            ", FILE_KEY=" + getFile_key() +
            ", DELETE_TAG=" + getDelete_tag() +
            ", ORDER_ID=" + getOrder_id() +
            ", GROUP_ID=" + getGroup_id() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
            ", MEMO=" + getMemo() +
    "}";
    }
}
