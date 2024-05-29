package com.lubase.wfengine.model;

import lombok.Data;

@Data
public class WFCmdModel {
    private String id;
    private String cmd_type;
    private String cmd_des;
    private Integer order_id;
    //审批意见是否必填
    private Integer require_process_memo;

}
