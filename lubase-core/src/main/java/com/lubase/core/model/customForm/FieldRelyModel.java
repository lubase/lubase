package com.lubase.core.model.customForm;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldRelyModel implements Serializable {
    private static final long serialVersionUID = 1199427090177331200L;
    private String field;
    private String relyFields;
}
