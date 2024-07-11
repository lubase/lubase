package com.lubase.core.model.customForm;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldRelyModel implements Serializable {
    private String field;
    private String relyFields;
}
