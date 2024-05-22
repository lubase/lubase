package com.lcp.qibao.auto.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;

import java.time.LocalDateTime;

/**
* hello custom
* <p>
    * 页面按钮
    * </p>
*
* @author A
* @since 2022-06-03
*/
@TableName("ss_button")
public class SsButtonEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "ss_button";
    public static final String COL_ID = "id";
    public static final String COL_PAGE_ID = "page_id";
    public static final String COL_BUTTON_NAME = "button_name";
    public static final String COL_METHOD_ID = "method_id";
    public static final String COL_FORM_ID = "form_id";
    public static final String COL_BUTTON_TYPE = "button_type";
    public static final String COL_DISPLAY_TYPE = "display_type";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_GROUP_DES = "group_des";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_NAV_ADDRESS = "nav_address";
    public static final String COL_RENDER_SETTING = "render_setting";
    public static final String COL_SERVER_SETTING = "server_setting";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_LINK_COLUMN = "link_column";

    public Long getPage_id() {
        return TypeConverterUtils.object2Long(super.get("page_id"));
    }

    public void setPage_id(Long page_id) {
        super.put("page_id",page_id);
    }
    public String getButton_name() {
        return TypeConverterUtils.object2String(super.get("button_name"));
    }

    public void setButton_name(String button_name) {
        super.put("button_name",button_name);
    }
    public Long getMethod_id() {
        return TypeConverterUtils.object2Long(super.get("method_id"));
    }

    public void setMethod_id(Long method_id) {
        super.put("method_id",method_id);
    }
    public Long getForm_id() {
        return TypeConverterUtils.object2Long(super.get("form_id"));
    }

    public void setForm_id(Long form_id) {
        super.put("form_id",form_id);
    }
    public String getButton_type() {
        return TypeConverterUtils.object2String(super.get("button_type"));
    }

    public void setButton_type(String button_type) {
        super.put("button_type",button_type);
    }
    public String getDisplay_type() {
        return TypeConverterUtils.object2String(super.get("display_type"));
    }

    public void setDisplay_type(String display_type) {
        super.put("display_type",display_type);
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
    public String getGroup_des() {
        return TypeConverterUtils.object2String(super.get("group_des"));
    }

    public void setGroup_des(String group_des) {
        super.put("group_des",group_des);
    }
    public String getDescription() {
        return TypeConverterUtils.object2String(super.get("description"));
    }

    public void setDescription(String description) {
        super.put("description",description);
    }
    public String getNav_address() {
        return TypeConverterUtils.object2String(super.get("nav_address"));
    }

    public void setNav_address(String nav_address) {
        super.put("nav_address",nav_address);
    }
    public String getRender_setting() {
        return TypeConverterUtils.object2String(super.get("render_setting"));
    }

    public void setRender_setting(String render_setting) {
        super.put("render_setting",render_setting);
    }
    public String getServer_setting() {
        return TypeConverterUtils.object2String(super.get("server_setting"));
    }

    public void setServer_setting(String server_setting) {
        super.put("server_setting",server_setting);
    }
    public String getCreate_by() {
        return TypeConverterUtils.object2String(super.get("create_by"));
    }

    public void setCreate_by(String create_by) {
        super.put("create_by",create_by);
    }
    public LocalDateTime getCreate_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("create_time"));
    }

    public void setCreate_time(LocalDateTime create_time) {
        super.put("create_time",create_time);
    }
    public String getUpdate_by() {
        return TypeConverterUtils.object2String(super.get("update_by"));
    }

    public void setUpdate_by(String update_by) {
        super.put("update_by",update_by);
    }
    public LocalDateTime getUpdate_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("update_time"));
    }

    public void setUpdate_time(LocalDateTime update_time) {
        super.put("update_time",update_time);
    }
    public String getLink_column() {
        return TypeConverterUtils.object2String(super.get("link_column"));
    }

    public void setLink_column(String link_column) {
        super.put("link_column",link_column);
    }

    @Override
    public String toString() {
        return "ss_button2{" +
                "ID=" + getId() +
                ", PAGE_ID=" + getPage_id() +
                ", BUTTON_NAME=" + getButton_name() +
                ", METHOD_ID=" + getMethod_id() +
                ", FORM_ID=" + getForm_id() +
                ", BUTTON_TYPE=" + getButton_type() +
                ", DISPLAY_TYPE=" + getDisplay_type() +
                ", DELETE_TAG=" + getDelete_tag() +
                ", ORDER_ID=" + getOrder_id() +
                ", GROUP_DES=" + getGroup_des() +
                ", DESCRIPTION=" + getDescription() +
                ", NAV_ADDRESS=" + getNav_address() +
                ", RENDER_SETTING=" + getRender_setting() +
                ", SERVER_SETTING=" + getServer_setting() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                ", LINK_COLUMN=" + getLink_column() +
                "}";
    }
}
