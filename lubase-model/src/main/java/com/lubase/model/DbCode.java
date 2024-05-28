package com.lubase.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 代码表实体类
 */
@Data
public class DbCode implements Serializable {
    private static final long serialVersionUID = 5584605329383028412L;
    private Long id;
    private Long code_type_id;
    private String code;
    private String name;
    private Integer enable_tag;
    private Integer order_id;
}
