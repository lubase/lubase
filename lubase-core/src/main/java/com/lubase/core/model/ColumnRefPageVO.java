package com.lubase.core.model;

import lombok.Data;

/**
 * 自定义弹窗 关联的页面信息
 */

@Data
public class ColumnRefPageVO {
    /**
     * 页面id
     */
    private String pageId;
    /**
     * 页面code
     */
    private String pageCode;
    /**
     * 值列
     */
    private String tableKey;
    /**
     * 用于显示的列
     */
    private String displayCol;
}
