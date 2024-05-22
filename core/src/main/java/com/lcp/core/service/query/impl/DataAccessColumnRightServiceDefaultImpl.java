package com.lcp.core.service.query.impl;

import com.lcp.core.service.query.DataAccessColumnRightService;
import com.lcp.coremodel.DbField;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataAccessColumnRightServiceDefaultImpl implements DataAccessColumnRightService {
    @Override
    public List<DbField> checkAccessRight(List<DbField> fieldList) {
        return fieldList;
    }
}
