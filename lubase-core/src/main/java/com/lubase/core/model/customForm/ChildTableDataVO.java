package com.lubase.core.model.customForm;

import com.lubase.core.model.SearchVO;
import com.lubase.orm.model.DbCollection;
import lombok.Data;

/**
 * 表单子表返回到客户端的数据对象
 */
@Data
public class ChildTableDataVO {
    /**
     * 数据对象
     */
    private DbCollection dbCollection;
    /**
     * 搜索区域配置
     */
    private SearchVO search;
}
