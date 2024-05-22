package com.lcp.qibao.model.customForm;

import com.lcp.core.QueryOption;
import lombok.Data;

@Data
public class ChildTableSetting {
    /**
     * 随机id
     */
    private String serialNum;

    private QueryOption queryOption;
}
