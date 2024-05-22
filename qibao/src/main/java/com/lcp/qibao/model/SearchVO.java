package com.lcp.qibao.model;

import com.lcp.core.TableFilter;
import com.lcp.coremodel.DbField;
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
