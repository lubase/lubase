package com.lubase.model;

import lombok.Data;

/**
 * 多语言配置
 */
@Data
public class ResourceDataModel {
    private String tableId;
    private String dataId;
    private String field;
    private String userLanguage;
    private String msg;
}
