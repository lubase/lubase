package com.lubase.core.model.customForm;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldControlModel implements Serializable {
    private static final long serialVersionUID = 1199426962745987072L;
    private String field;
    private String controlRules;
}
