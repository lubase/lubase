package com.lubase.sso.model;

import lombok.Data;

@Data
public class SSODataBo {
    private  Integer code;
    private String key;
    private  String message;
    public SSOResultDataBo result;
}
