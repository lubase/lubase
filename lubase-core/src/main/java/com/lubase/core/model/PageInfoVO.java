package com.lubase.core.model;

import lombok.Data;

import java.util.List;

/**
 * @author A
 */
@Data
public class PageInfoVO {
    private String tmp;
    private List<ButtonVO> btns;
    private SearchVO search;
    private String treeInfo;
    private String gridInfo;
    /**
     * 格式为 对象序列化后的字符安。例如："{\"AA\":\"val1\",\"BB\":\"val2\"}"
     */
    private String customParam;
    /**
     * 页面banner设置。例如：{"content":"","methodId":""}
     */
    private String bannerSetting;
    private String formSetting;
}
