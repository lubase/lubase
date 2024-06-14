package com.lubase.core.model;

import lombok.Data;

/**
 * 弹窗选人数据对象
 */
@Data
public class SelectUserModel {
    private String id;
    private String userCode;
    private String userName;
    private String deptId;
    private String deptName;
}
