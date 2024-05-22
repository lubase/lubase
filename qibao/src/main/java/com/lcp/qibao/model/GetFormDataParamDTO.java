package com.lcp.qibao.model;

import lombok.Data;

@Data
public class GetFormDataParamDTO {
    private String funcCode;
    private String pageId;
    private String formId;
    private String id;
    private String clientMacro;
}
