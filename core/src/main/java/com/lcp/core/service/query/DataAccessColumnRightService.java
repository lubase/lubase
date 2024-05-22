package com.lcp.core.service.query;

import com.lcp.coremodel.DbField;

import java.util.List;

/**
 * 列权限
 */
public interface DataAccessColumnRightService {
    List<DbField> checkAccessRight(List<DbField> fieldList);
}
