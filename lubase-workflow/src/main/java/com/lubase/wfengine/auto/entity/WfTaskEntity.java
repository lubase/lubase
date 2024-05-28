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
    @TableName("wf_task")
    public class WfTaskEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_task";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_TASK_NAME = "task_name";
    public static final String COL_TASK_TYPE = "task_type";
    public static final String COL_POSITION_X = "position_x";
    public static final String COL_POSITION_Y = "position_y";
    public static final String COL_MEMO = "memo";
    public static final String COL_DATA_TEMPLATE = "data_template";
    public static final String COL_SETTING = "setting";
    public static final String COL_REF_PAGE_SETTING = "ref_page_setting";
    public static final String COL_REBUILD_OPERATOR = "rebuild_operator";
    public static final String COL_TIMEOUT = "timeout";

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
    public String getFlow_id() {
        return TypeConverterUtils.object2String(super.get("flow_id"));
    }

    public void setFlow_id(String flow_id) {
        super.put("flow_id",flow_id);
    }
    public String getTask_name() {
        return TypeConverterUtils.object2String(super.get("task_name"));
    }

    public void setTask_name(String task_name) {
        super.put("task_name",task_name);
    }
    public String getTask_type() {
        return TypeConverterUtils.object2String(super.get("task_type"));
    }

    public void setTask_type(String task_type) {
        super.put("task_type",task_type);
    }
    public String getPosition_x() {
        return TypeConverterUtils.object2String(super.get("position_x"));
    }

    public void setPosition_x(String position_x) {
        super.put("position_x",position_x);
    }
    public String getPosition_y() {
        return TypeConverterUtils.object2String(super.get("position_y"));
    }

    public void setPosition_y(String position_y) {
        super.put("position_y",position_y);
    }
    public String getMemo() {
        return TypeConverterUtils.object2String(super.get("memo"));
    }

    public void setMemo(String memo) {
        super.put("memo",memo);
    }
    public String getData_template() {
        return TypeConverterUtils.object2String(super.get("data_template"));
    }

    public void setData_template(String data_template) {
        super.put("data_template",data_template);
    }
    public String getSetting() {
        return TypeConverterUtils.object2String(super.get("setting"));
    }

    public void setSetting(String setting) {
        super.put("setting",setting);
    }
    public String getRef_page_setting() {
        return TypeConverterUtils.object2String(super.get("ref_page_setting"));
    }

    public void setRef_page_setting(String ref_page_setting) {
        super.put("ref_page_setting",ref_page_setting);
    }
    public Boolean getRebuild_operator() {
        return TypeConverterUtils.object2Boolean(super.get("rebuild_operator"));
    }

    public void setRebuild_operator(Boolean rebuild_operator) {
        super.put("rebuild_operator",rebuild_operator);
    }
    public Integer getTimeout() {
        return TypeConverterUtils.object2Integer(super.get("timeout"));
    }

    public void setTimeout(Integer timeout) {
        super.put("timeout",timeout);
    }

    @Override
    public String toString() {
        return "WfTaskEntity2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", FLOW_ID=" + getFlow_id() +
                ", TASK_NAME=" + getTask_name() +
                ", TASK_TYPE=" + getTask_type() +
                ", POSITION_X=" + getPosition_x() +
                ", POSITION_Y=" + getPosition_y() +
                ", MEMO=" + getMemo() +
                ", DATA_TEMPLATE=" + getData_template() +
                ", SETTING=" + getSetting() +
                ", REF_PAGE_SETTING=" + getRef_page_setting() +
                ", REBUILD_OPERATOR=" + getRebuild_operator() +
                ", TIMEOUT=" + getTimeout() +
                "}";
    }
}
