package com.lubase.starter.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 按钮VO
 *
 * @author A
 */
@Data
public class ButtonVO implements Serializable {
    private static final long serialVersionUID = 1026242102037581824L;
    private String id;
    @Deprecated
    private String code;
    private String name;
    private String btnType;
    private String disType;
    private Integer orderId;
    private String groupDes;
    private String navAddress;
    private String linkColumn;
    private String renderSetting;
}
