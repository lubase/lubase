package com.lubase.sso.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OAOtherInfoVo {
    private  Long orgId;
    private  String userName;
    private  String phone;
    private Map<String,String> refData;

    public OAOtherInfoVo() {
        refData=new HashMap<>();
    }
}
