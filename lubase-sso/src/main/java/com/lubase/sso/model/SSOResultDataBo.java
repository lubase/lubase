package com.lubase.sso.model;

import lombok.Data;

@Data
public class SSOResultDataBo {
    public Integer is_formal;
    public String user_id;
    public String user_code;
    public String user_name;
    public String group_id;
    public String group_name;
    public String duty_name;
    public String position_name;
}
