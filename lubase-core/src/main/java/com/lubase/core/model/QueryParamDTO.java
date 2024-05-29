package com.lubase.core.model;

import lombok.Data;

@Data
public class QueryParamDTO {
    private int pageSize;
    private int pageIndex;
    /**
     * 设置排序，例如：  orderid asc|  suuser.orderid asc,suuser.age desc
     */
    private String sortField;
}
