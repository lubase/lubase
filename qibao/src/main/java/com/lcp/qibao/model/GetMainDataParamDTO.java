package com.lcp.qibao.model;

import lombok.Data;

@Data
public class GetMainDataParamDTO {
    private String pageId;
    private String clientMacro;
    private String fullTextSearch;
    private String searchParam;
    private String queryParam;
}
