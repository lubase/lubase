package com.lubase.wfengine.service;

import com.lubase.orm.TableFilter;
import com.lubase.model.DbEntity;

import java.util.function.Predicate;

public interface DynamicPredicateService {
    Predicate<DbEntity> parseTableFilterToPredicate(TableFilter tableFilter);
}
