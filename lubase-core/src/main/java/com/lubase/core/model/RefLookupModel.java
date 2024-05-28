package com.lubase.core.model;

import lombok.Data;

/**
 * description: 关联数据表
 * created on 2019/8/19
 */
@Data
public class RefLookupModel {
    /**
     * 值列
     */
    private String tableKey;
    /**
     * 用于显示的列
     */
    private String displayCol;
    /**
     * 用于增加显示信息的列，单个
     */
    private String extendCol;
    /**
     * 用于搜索区域的列
     */
    private String searchCols;
    /**
     * 用户查询显示列
     */
    private String selectCols;
    /**
     * 表类型
     */
    private String tableType;
    /**
     * 表代码
     */
    private String tableCode;

    /**
     * 关联的页面id
     */
    private String pageId;
}
