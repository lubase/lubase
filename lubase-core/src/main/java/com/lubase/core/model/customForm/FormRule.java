package com.lubase.core.model.customForm;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FormRule implements Serializable {
    /**
     * 表单字段级联控制规则
     */
    private List<FieldControlModel> fieldControl;
    /**
     * 表单字段级联过滤规则
     */
    private List<FieldRelyModel> fieldRely;
}
