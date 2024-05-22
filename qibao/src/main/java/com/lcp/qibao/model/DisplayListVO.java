package com.lcp.qibao.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DisplayListVO implements Serializable {
    private static final long serialVersionUID = 1026242722349977600L;

    /**
     * 页面标识
     */
    private Long pageId;
    /**
     * 用户标识
     */
    private Long accountId;
    /**
     * 显示列数量
     */
    private String columnIds;
    /**
     * 锁定列数量
     */
    private int lockColumnCount;
    /**
     * 列宽度设置
     */
    private String columnWidthSetting;
}


