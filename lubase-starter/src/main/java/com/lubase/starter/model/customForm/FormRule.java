package com.lubase.starter.model.customForm;


import lombok.Data;

import java.util.List;

@Data
public class FormRule {
    /**
     * 表单字段级联控制规则
     */
    private List<FieldControlModel> fieldControl;
    /**
     * 表单字段级联过滤规则
     */
    private List<FieldRelyModel> fieldRely;

    /**
     * 表单字段回填规则
     */
    private List<FieldBackfillModel> fieldBackfill;
}
