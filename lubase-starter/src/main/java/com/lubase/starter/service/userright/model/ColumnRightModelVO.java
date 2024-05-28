package com.lubase.starter.service.userright.model;

import lombok.Data;

@Data
public class ColumnRightModelVO {
    private Long tableId;
    private Long columnId;
    private String columnDisplayName;
    private int accessRight;
}
