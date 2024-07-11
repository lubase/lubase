package com.lubase.core.model.customForm;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldControlModel implements Serializable {
    private String field;
    private String controlRules;
}
