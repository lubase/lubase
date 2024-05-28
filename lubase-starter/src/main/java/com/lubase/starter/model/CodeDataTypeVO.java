package com.lubase.starter.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author A
 */
@Data
public class CodeDataTypeVO implements Serializable {
    private static final long serialVersionUID = 1026242384641396736L;

    private Long codeDataTypeId;
    private List<CodeDataVO> codeList;
}
