package com.lubase.model;

import lombok.Data;

/**
 * 多语言配置
 */
@Data
public class ResourceDataModel {
    private static final long serialVersionUID = 5584605365483028412L;
    private String tableId;
    private String dataId;
    private String field;
    private String userLanguage;
    private String msg;
}
