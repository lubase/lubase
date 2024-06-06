package com.lubase.core.model;

import lombok.Data;

@Data
public class ButtonRefFormInfo {
    /**
     * 按钮类型
     */
    private String buttonType;
    /**
     * 关联表单信息
     */
    private String refFormId;
    /**
     * 是否是表单子表按钮
     */
    private Boolean isFormChildTable;
}
