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
    @TableName("wf_link")
    public class WfLinkEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "wf_link";
    public static final String COL_ID = "id";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_FLOW_ID = "flow_id";
    public static final String COL_BEGIN_TASK_ID = "begin_task_id";
    public static final String COL_END_TASK_ID = "end_task_id";
    public static final String COL_CMD_ID = "cmd_id";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_LOGIC_CONDITION = "logic_condition";
    public static final String COL_MEMO = "memo";
    public static final String COL_UPDATE_CONTENT = "update_content";

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
    public String getBegin_task_id() {
        return TypeConverterUtils.object2String(super.get("begin_task_id"));
    }

    public void setBegin_task_id(String begin_task_id) {
        super.put("begin_task_id",begin_task_id);
    }
    public String getEnd_task_id() {
        return TypeConverterUtils.object2String(super.get("end_task_id"));
    }

    public void setEnd_task_id(String end_task_id) {
        super.put("end_task_id",end_task_id);
    }
    public String getCmd_id() {
        return TypeConverterUtils.object2String(super.get("cmd_id"));
    }

    public void setCmd_id(String cmd_id) {
        super.put("cmd_id",cmd_id);
    }
    public Integer getPriority() {
        return TypeConverterUtils.object2Integer(super.get("priority"));
    }

    public void setPriority(Integer priority) {
        super.put("priority",priority);
    }
    public String getLogic_condition() {
        return TypeConverterUtils.object2String(super.get("logic_condition"));
    }

    public void setLogic_condition(String logic_condition) {
        super.put("logic_condition",logic_condition);
    }
    public String getMemo() {
        return TypeConverterUtils.object2String(super.get("memo"));
    }

    public void setMemo(String memo) {
        super.put("memo",memo);
    }
    public String getUpdate_content() {
        return TypeConverterUtils.object2String(super.get("update_content"));
    }

    public void setUpdate_content(String update_content) {
        super.put("update_content",update_content);
    }

    @Override
    public String toString() {
        return "WfLinkEntity2{" +
                "ID=" + getId() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", FLOW_ID=" + getFlow_id() +
                ", BEGIN_TASK_ID=" + getBegin_task_id() +
                ", END_TASK_ID=" + getEnd_task_id() +
                ", CMD_ID=" + getCmd_id() +
                ", PRIORITY=" + getPriority() +
                ", LOGIC_CONDITION=" + getLogic_condition() +
                ", MEMO=" + getMemo() +
                ", UPDATE_CONTENT=" + getUpdate_content() +
                "}";
    }
}
