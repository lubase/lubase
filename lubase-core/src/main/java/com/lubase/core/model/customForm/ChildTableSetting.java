package com.lubase.core.model.customForm;

import com.lubase.orm.QueryOption;
import lombok.Data;

@Data
public class ChildTableSetting {
    /**
     * 随机id
     */
    private String serialNum;

    /**
     * 查询配置
     */
    private QueryOption queryOption;

    /**
     * 搜索信息
     */
    private String searchInfo;
}
