package com.lcp.qibao.model.customForm;

import lombok.Data;

/**
 * 表单中用于扩展注册列信息的数据对象
 *
 * @author A
 */
@Data
public class DbFieldExtend {
    private String id;
    private int orderId;
    private int colSpan;
    private int rowSpan;
    private Boolean isNull;
    private int fieldAccess;
    /**
     * 元素类型。1:普通字段  2：插槽 3:分割线
     */
    private int eleType = 1;
}
