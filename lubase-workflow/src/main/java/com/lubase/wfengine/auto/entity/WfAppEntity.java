package com.lubase.wfengine.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 
    * </p>
*
* @author shishuai
* @since 2022-11-27
*/
    @TableName("wf_app")
    public class WfAppEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "wf_app";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_NAME = "name";
    public static final String COL_PLATFORM_CODE = "platform_code";
    public static final String COL_CALLBACK_URL = "callback_url";

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
        public String getName() {
            return TypeConverterUtils.object2String(super.get("name"));
        }

            public void setName(String name) {
        super.put("name",name);
        }
        public String getPlatform_code() {
            return TypeConverterUtils.object2String(super.get("platform_code"));
        }

            public void setPlatform_code(String platform_code) {
        super.put("platform_code",platform_code);
        }
        public String getCallback_url() {
            return TypeConverterUtils.object2String(super.get("callback_url"));
        }

            public void setCallback_url(String callback_url) {
        super.put("callback_url",callback_url);
        }

    @Override
    public String toString() {
    return "WfAppEntity{" +
            "ID=" + getId() +
            ", CREATE_BY=" + getCreate_by() +
            ", CREATE_TIME=" + getCreate_time() +
            ", UPDATE_BY=" + getUpdate_by() +
            ", UPDATE_TIME=" + getUpdate_time() +
            ", NAME=" + getName() +
            ", PLATFORM_CODE=" + getPlatform_code() +
            ", CALLBACK_URL=" + getCallback_url() +
    "}";
    }
}
