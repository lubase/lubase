package com.lubase.core.model.customForm;

import lombok.Data;

@Data
public class FormFieldInfo {
    /**
     * 字段id
     */
    private String id;
    /**
     * 必填标记
     */
    private Integer required;
    /**
     * 只读标记
     */
    private Integer readonly;
}
