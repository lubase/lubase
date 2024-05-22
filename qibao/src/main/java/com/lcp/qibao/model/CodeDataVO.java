package com.lcp.qibao.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CodeDataVO implements Serializable {
    private static final long serialVersionUID = 1026242384641396723L;
    private String code;
    private String name;
    private String pydm;
    private String pCode;
    private Integer enableTag;
    private int orderId;
}
