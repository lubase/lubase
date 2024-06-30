package com.lubase.sso.model;

import lombok.Data;

@Data
public class OAUserVo {
    private String fieldValue;
    private String fieldName;
    private  OAOtherInfoVo otherField;
}
