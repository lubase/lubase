package com.lubase.core.service.userright.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnRightModelVO implements Serializable {
    private static final long serialVersionUID = 1198969620812271616L;
    private Long tableId;
    private Long columnId;
    private String columnDisplayName;
    private int accessRight;
}
