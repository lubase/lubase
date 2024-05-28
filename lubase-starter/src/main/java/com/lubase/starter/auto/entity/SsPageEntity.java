package com.lubase.starter.auto.entity;

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
 * @author A
 * @since 2022-08-03
 */
@TableName("ss_page")
public class SsPageEntity extends DbEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_CODE = "ss_page";
    public static final String COL_ID = "id";
    public static final String COL_APP_ID = "app_id";
    public static final String COL_PAGE_CODE = "page_code";
    public static final String COL_PAGE_NAME = "page_name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PARENT_ID = "parent_id";
    public static final String COL_MASTER_PAGE = "master_page";
    public static final String COL_GRID_INFO = "grid_info";
    public static final String COL_GRID_QUERY = "grid_query";
    public static final String COL_CUSTOM_PARAMS = "custom_params";
    public static final String COL_SEARCH_FILTER = "search_filter";
    public static final String COL_TREE_INFO = "tree_info";
    public static final String COL_TREE_QUERY = "tree_query";
    public static final String COL_ORDER_ID = "order_id";
    public static final String COL_METHODS = "methods";
    public static final String COL_VISIBLE = "visible";
    public static final String COL_TYPE = "type";
    public static final String COL_DELETE_TAG = "delete_tag";
    public static final String COL_VUE_COMPONENT = "vue_component";
    public static final String COL_ICON_CODE = "icon_code";
    public static final String COL_VUE_ROUTER = "vue_router";
    public static final String COL_STATISTICS_SETTING = "statistics_setting";
    public static final String COL_BANNER_SETTING = "banner_setting";
    public static final String COL_FORM_SETTING = "form_setting";
    public static final String COL_CAN_OPERATE_FORMS = "can_operate_forms";
    public static final String COL_WORKFLOW_SETTING = "workflow_setting";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_TIME = "update_time";

    public Long getApp_id() {
        return TypeConverterUtils.object2Long(super.get("app_id"));
    }

    public void setApp_id(Long app_id) {
        super.put("app_id", app_id);
    }

    public String getPage_code() {
        return TypeConverterUtils.object2String(super.get("page_code"));
    }

    public void setPage_code(String page_code) {
        super.put("page_code", page_code);
    }

    public String getPage_name() {
        return TypeConverterUtils.object2String(super.get("page_name"));
    }

    public void setPage_name(String page_name) {
        super.put("page_name", page_name);
    }

    public String getDescription() {
        return TypeConverterUtils.object2String(super.get("description"));
    }

    public void setDescription(String description) {
        super.put("description", description);
    }

    public Long getParent_id() {
        return TypeConverterUtils.object2Long(super.get("parent_id"));
    }

    public void setParent_id(Long parent_id) {
        super.put("parent_id", parent_id);
    }

    public String getMaster_page() {
        return TypeConverterUtils.object2String(super.get("master_page"));
    }

    public void setMaster_page(String master_page) {
        super.put("master_page", master_page);
    }

    public String getGrid_info() {
        return TypeConverterUtils.object2String(super.get("grid_info"));
    }

    public void setGrid_info(String grid_info) {
        super.put("grid_info", grid_info);
    }

    public String getGrid_query() {
        return TypeConverterUtils.object2String(super.get("grid_query"));
    }

    public void setGrid_query(String grid_query) {
        super.put("grid_query", grid_query);
    }

    public String getCustom_params() {
        return TypeConverterUtils.object2String(super.get("custom_params"));
    }

    public void setCustom_params(String custom_params) {
        super.put("custom_params", custom_params);
    }

    public String getSearch_filter() {
        return TypeConverterUtils.object2String(super.get("search_filter"));
    }

    public void setSearch_filter(String search_filter) {
        super.put("search_filter", search_filter);
    }

    public String getTree_info() {
        return TypeConverterUtils.object2String(super.get("tree_info"));
    }

    public void setTree_info(String tree_info) {
        super.put("tree_info", tree_info);
    }

    public String getTree_query() {
        return TypeConverterUtils.object2String(super.get("tree_query"));
    }

    public void setTree_query(String tree_query) {
        super.put("tree_query", tree_query);
    }

    public Integer getOrder_id() {
        return TypeConverterUtils.object2Integer(super.get("order_id"));
    }

    public void setOrder_id(Integer order_id) {
        super.put("order_id", order_id);
    }

    public String getMethods() {
        return TypeConverterUtils.object2String(super.get("methods"));
    }

    public void setMethods(String methods) {
        super.put("methods", methods);
    }

    public Boolean getVisible() {
        return TypeConverterUtils.object2Boolean(super.get("visible"));
    }

    public void setVisible(Boolean visible) {
        super.put("visible", visible);
    }

    public Integer getType() {
        return TypeConverterUtils.object2Integer(super.get("type"));
    }

    public void setType(Integer type) {
        super.put("type", type);
    }

    public Boolean getDelete_tag() {
        return TypeConverterUtils.object2Boolean(super.get("delete_tag"));
    }

    public void setDelete_tag(Boolean delete_tag) {
        super.put("delete_tag", delete_tag);
    }

    public String getVue_component() {
        return TypeConverterUtils.object2String(super.get("vue_component"));
    }

    public void setVue_component(String vue_component) {
        super.put("vue_component", vue_component);
    }

    public String getIcon_code() {
        return TypeConverterUtils.object2String(super.get("icon_code"));
    }

    public void setIcon_code(String icon_code) {
        super.put("icon_code", icon_code);
    }

    public String getVue_router() {
        return TypeConverterUtils.object2String(super.get("vue_router"));
    }

    public void setVue_router(String vue_router) {
        super.put("vue_router", vue_router);
    }

    public String getStatistics_setting() {
        return TypeConverterUtils.object2String(super.get("statistics_setting"));
    }

    public void setStatistics_setting(String statistics_setting) {
        super.put("statistics_setting", statistics_setting);
    }

    public String getBanner_setting() {
        return TypeConverterUtils.object2String(super.get("banner_setting"));
    }

    public void setBanner_setting(String banner_setting) {
        super.put("banner_setting", banner_setting);
    }

    public String getForm_setting() {
        return TypeConverterUtils.object2String(super.get("form_setting"));
    }

    public void setForm_setting(String form_setting) {
        super.put("form_setting", form_setting);
    }

    public String getCan_operate_forms() {
        return TypeConverterUtils.object2String(super.get("can_operate_forms"));
    }

    public void setCan_operate_forms(String can_operate_forms) {
        super.put("can_operate_forms", can_operate_forms);
    }

    public String getWorkflow_setting() {
        return TypeConverterUtils.object2String(super.get("workflow_setting"));
    }

    public void setWorkflow_setting(String workflow_setting) {
        super.put("workflow_setting", workflow_setting);
    }

    public Long getCreate_by() {
        return TypeConverterUtils.object2Long(super.get("create_by"));
    }

    public void setCreate_by(Long create_by) {
        super.put("create_by", create_by);
    }

    public LocalDateTime getCreate_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("create_time"));
    }

    public void setCreate_time(LocalDateTime create_time) {
        super.put("create_time", create_time);
    }

    public Long getUpdate_by() {
        return TypeConverterUtils.object2Long(super.get("update_by"));
    }

    public void setUpdate_by(Long update_by) {
        super.put("update_by", update_by);
    }

    public LocalDateTime getUpdate_time() {
        return TypeConverterUtils.object2LocalDateTime(super.get("update_time"));
    }

    public void setUpdate_time(LocalDateTime update_time) {
        super.put("update_time", update_time);
    }

    @Override
    public String toString() {
        return "ss_page2{" +
                "ID=" + getId() +
                ", APP_ID=" + getApp_id() +
                ", PAGE_CODE=" + getPage_code() +
                ", PAGE_NAME=" + getPage_name() +
                ", DESCRIPTION=" + getDescription() +
                ", PARENT_ID=" + getParent_id() +
                ", MASTER_PAGE=" + getMaster_page() +
                ", GRID_INFO=" + getGrid_info() +
                ", GRID_QUERY=" + getGrid_query() +
                ", CUSTOM_PARAMS=" + getCustom_params() +
                ", SEARCH_FILTER=" + getSearch_filter() +
                ", TREE_INFO=" + getTree_info() +
                ", TREE_QUERY=" + getTree_query() +
                ", ORDER_ID=" + getOrder_id() +
                ", METHODS=" + getMethods() +
                ", VISIBLE=" + getVisible() +
                ", TYPE=" + getType() +
                ", DELETE_TAG=" + getDelete_tag() +
                ", VUE_COMPONENT=" + getVue_component() +
                ", ICON_CODE=" + getIcon_code() +
                ", VUE_ROUTER=" + getVue_router() +
                ", STATISTICS_SETTING=" + getStatistics_setting() +
                ", BANNER_SETTING=" + getBanner_setting() +
                ", FORM_SETTING=" + getForm_setting() +
                ", CAN_OPERATE_FORMS=" + getCan_operate_forms() +
                ", WORKFLOW_SETTING=" + getWorkflow_setting() +
                ", CREATE_BY=" + getCreate_by() +
                ", CREATE_TIME=" + getCreate_time() +
                ", UPDATE_BY=" + getUpdate_by() +
                ", UPDATE_TIME=" + getUpdate_time() +
                "}";
    }
}
