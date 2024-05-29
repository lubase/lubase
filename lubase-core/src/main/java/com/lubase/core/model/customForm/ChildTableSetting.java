package com.lubase.core.model.customForm;

import com.lubase.orm.QueryOption;
import lombok.Data;

@Data
public class ChildTableSetting {
    /**
     * 随机id
     */
    private String serialNum;

    private QueryOption queryOption;
}
