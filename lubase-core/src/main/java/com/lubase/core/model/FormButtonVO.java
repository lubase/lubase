package com.lubase.core.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FormButtonVO implements Serializable {

    private Long id;
    /**
     * 按钮唯一标识
     */
    private String code;
    private String name;
    private String btnType;
    private String disType;
    private Integer orderId;
    private String groupDes;
    private String serialNum;
    private String renderSetting;
}
