package com.lubase.starter.model;

import com.lubase.model.DbField;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author A
 */
@Data
public class SearchVO {
    private List<SearchCondition> filter;
    private Map<String, DbField> fieldInfo;
}
