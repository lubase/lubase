package com.lubase.core.model;

import lombok.Data;

@Data
public class UserInfoModel {
    private Long Id;
    private String userCode;
    private String userName;
    /**
     * 账号是否启用
     */
    private boolean enableTag;
}
