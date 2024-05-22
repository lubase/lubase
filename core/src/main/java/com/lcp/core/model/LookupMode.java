package com.lcp.core.model;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @author A
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LookupMode {
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
     * 关联的页面id
     */
    private String pageId;
    /**
     * 表类型
     */
    private String tableType;
    /**
     * 表代码
     */
    private String tableCode;

    public LookupMode(String tableKey, String displayCol, String tableCode) {
        this.tableKey = tableKey;
        this.displayCol = displayCol;
        this.tableCode = tableCode;
    }

    public static LookupMode FromJsonStr(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        return JSON.parseObject(jsonStr, LookupMode.class);
    }
}
