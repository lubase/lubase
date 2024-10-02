package com.lubase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 多语言配置
 */
@Data
public class ResourceDataModel implements Serializable {
    private static final long serialVersionUID = 5584605365483028412L;
    @JsonProperty("t")
    private String tableId;
    @JsonProperty("d")
    private String dataId;
    @JsonProperty("f")
    private String field;
    @JsonProperty("u")
    private String userLanguage;
    @JsonProperty("m")
    private String msg;
}
