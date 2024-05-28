package com.lubase.starter.model.customForm;

import com.lubase.core.QueryOption;
import lombok.Data;

@Data
public class ChildTableSetting {
    /**
     * 随机id
     */
    private String serialNum;

    private QueryOption queryOption;
}
