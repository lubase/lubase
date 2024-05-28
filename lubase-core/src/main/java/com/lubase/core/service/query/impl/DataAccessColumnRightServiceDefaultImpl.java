package com.lubase.core.service.query.impl;

import com.lubase.core.service.query.DataAccessColumnRightService;
import com.lubase.model.DbField;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataAccessColumnRightServiceDefaultImpl implements DataAccessColumnRightService {
    @Override
    public List<DbField> checkAccessRight(List<DbField> fieldList) {
        return fieldList;
    }
}
