package com.lubase.wfengine.auto.entity;

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
* @author shishuai
* @since 2022-11-27
*/
    @TableName("wf_service")
    public class WfServiceEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_service";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_APP_ID = "app_id";
    public static final String COL_NAME = "name";
    public static final String COL_DATA_TEMPLATE = "data_template";
    public static final String COL_CURRENT_FLOW_ID = "current_flow_id";
    public static final String COL_FORM_ID = "form_id";
    public static final String COL_MAIN_TABLE = "main_table";
    public static final String COL_FINS_NAME_TEMPLATE = "fins_name_template";
    public static final String COL_MODIFY_ON_RUNING = "modify_on_runing";
    public static final String COL_START_UPDATE = "start_update";
    public static final String COL_APPROVED_UPDATE = "approved_update";
    public static final String COL_GIVE_UP_UPDATE = "give_up_update";
    public static final String COL_REJECT_UPDATE = "reject_update";

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
    public Long getApp_id() {
        return TypeConverterUtils.object2Long(super.get("app_id"));
    }

    public void setApp_id(Long app_id) {
        super.put("app_id",app_id);
    }
    public String getName() {
        return TypeConverterUtils.object2String(super.get("name"));
    }

    public void setName(String name) {
        super.put("name",name);
    }
    public String getData_template() {
        return TypeConverterUtils.object2String(super.get("data_template"));
    }

    public void setData_template(String data_template) {
        super.put("data_template",data_template);
    }
    public String getCurrent_flow_id() {
        return TypeConverterUtils.object2String(super.get("current_flow_id"));
    }

    public void setCurrent_flow_id(String current_flow_id) {
        super.put("current_flow_id",current_flow_id);
    }
    public String getForm_id() {
        return TypeConverterUtils.object2String(super.get("form_id"));
    }

    public void setForm_id(String form_id) {
        super.put("form_id",form_id);
    }
    public String getMain_table() {
        return TypeConverterUtils.object2String(super.get("main_table"));
    }

    public void setMain_table(String main_table) {
        super.put("main_table",main_table);
    }
    public String getFins_name_template() {
        return TypeConverterUtils.object2String(super.get("fins_name_template"));
    }

    public void setFins_name_template(String fins_name_template) {
        super.put("fins_name_template",fins_name_template);
    }
    public Boolean getModify_on_runing() {
        return TypeConverterUtils.object2Boolean(super.get("modify_on_runing"));
    }

    public void setModify_on_runing(Boolean modify_on_runing) {
        super.put("modify_on_runing",modify_on_runing);
    }
    public String getStart_update() {
        return TypeConverterUtils.object2String(super.get("start_update"));
    }

    public void setStart_update(String start_update) {
        super.put("start_update",start_update);
    }
    public String getApproved_update() {
        return TypeConverterUtils.object2String(super.get("approved_update"));
    }

    public void setApproved_update(String approved_update) {
        super.put("approved_update",approved_update);
    }
    public String getGive_up_update() {
        return TypeConverterUtils.object2String(super.get("give_up_update"));
    }

    public void setGive_up_update(String give_up_update) {
        super.put("give_up_update",give_up_update);
    }
    public String getReject_update() {
        return TypeConverterUtils.object2String(super.get("reject_update"));
    }

    public void setReject_update(String reject_update) {
        super.put("reject_update",reject_update);
    }

    @Override
    public String toString() {
        return "WfServiceEntity2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", APP_ID=" + getApp_id() +
                ", NAME=" + getName() +
                ", DATA_TEMPLATE=" + getData_template() +
                ", CURRENT_FLOW_ID=" + getCurrent_flow_id() +
                ", FORM_ID=" + getForm_id() +
                ", MAIN_TABLE=" + getMain_table() +
                ", FINS_NAME_TEMPLATE=" + getFins_name_template() +
                ", MODIFY_ON_RUNING=" + getModify_on_runing() +
                ", START_UPDATE=" + getStart_update() +
                ", APPROVED_UPDATE=" + getApproved_update() +
                ", GIVE_UP_UPDATE=" + getGive_up_update() +
                ", REJECT_UPDATE=" + getReject_update() +
                "}";
    }
}
