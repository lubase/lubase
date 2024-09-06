package com.lubase.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 弹窗选人数据对象
 */
@Data
public class SelectUserModel {
    private String id;
    //设置json序列化后别名为user_code
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("organization_id")
    private String deptId;
    private String parentDeptId;
    private String deptName;
}
