package com.lubase.core.service.query;

import com.lubase.model.DbField;

import java.util.List;

/**
 * 列权限
 */
public interface DataAccessColumnRightService {
    List<DbField> checkAccessRight(List<DbField> fieldList);
}
