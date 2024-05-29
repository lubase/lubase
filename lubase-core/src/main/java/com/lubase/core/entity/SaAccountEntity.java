package com.lubase.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 系统账号
    * </p>
*
* @author A
* @since 2022-06-03
*/
    @TableName("sa_account")
    public class SaAccountEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
public static final String TABLE_CODE = "sa_account";
    public static final String COL_ID = "id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_UPDATED_BY = "updated_by";
    public static final String COL_UPDATED_TIME = "updated_time";
    public static final String COL_USER_CODE = "user_code";
    public static final String COL_PASSWORD = "password";

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
        public String getUser_code() {
            return TypeConverterUtils.object2String(super.get("user_code"));
        }

            public void setUser_code(String user_code) {
        super.put("user_code",user_code);
        }
        public String getPassword() {
            return TypeConverterUtils.object2String(super.get("password"));
        }

            public void setPassword(String password) {
        super.put("password",password);
        }

    @Override
    public String toString() {
    return "SaAccountEntity{" +
            "ID=" + getId() +
            ", CREATED_BY=" + getCreated_by() +
            ", CREATED_TIME=" + getCreated_time() +
            ", UPDATED_BY=" + getUpdated_by() +
            ", UPDATED_TIME=" + getUpdated_time() +
            ", USER_CODE=" + getUser_code() +
            ", PASSWORD=" + getPassword() +
    "}";
    }
}
