package com.lubase.wfengine.service.impl;

import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.service.DynamicPredicateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;

@Service
public class DynamicPredicateServiceImpl implements DynamicPredicateService {
    @SneakyThrows
    @Override
    public Predicate<DbEntity> parseTableFilterToPredicate(TableFilter tableFilter) {
        if (null == tableFilter) {
            return null;
        }
        Predicate<DbEntity> result = null;
        if (tableFilter.getChildFilters() != null) {
            for (TableFilter child : tableFilter.getChildFilters()) {
                Predicate<DbEntity> tmpResult = parseTableFilterToPredicate(child);
                if (tmpResult == null) {
                    continue;
                }
                if (result == null) {
                    result = tmpResult;
                } else {
                    if (tableFilter.isAnd()) {
                        result = result.and(tmpResult);
                    } else {
                        result = result.or(tmpResult);
                    }
                }
            }
        } else {
            return getChildPredicate(tableFilter);
        }
        if (tableFilter.isNot()) {
            throw new WarnCommonException("DynamicPredicate 不支持 在tableFilter中配置IsNot");
        }
        return result;
    }

    Predicate<DbEntity> getChildPredicate(TableFilter tableFilter) {
        String fieldName = tableFilter.getFilterName();
        Object filterValue = tableFilter.getFilterValue();
        EOperateMode operateMode = tableFilter.getOperateMode();
        Predicate<DbEntity> filter = null;
        if (StringUtils.isEmpty(fieldName)) {
            return filter;
        }
        if (StringUtils.isEmpty(filterValue) &&
                !operateMode.equals(EOperateMode.IsNull) && !operateMode.equals(EOperateMode.IsNotNull)) {
            return filter;
        }

        switch (operateMode) {
            case LikeAll:
                filter = d -> TypeConverterUtils.object2String(d.get(fieldName), "").contains(TypeConverterUtils.object2String(filterValue));
                break;
            case LikeLeft:
                filter = d -> TypeConverterUtils.object2String(d.get(fieldName), "").startsWith(TypeConverterUtils.object2String(filterValue));
                break;
            case LikeRight:
                filter = d -> TypeConverterUtils.object2String(d.get(fieldName), "").endsWith(TypeConverterUtils.object2String(filterValue));
                break;
            case Equals:
                filter = d -> TypeConverterUtils.object2String(d.get(fieldName), "").equals(TypeConverterUtils.object2String(filterValue));
                break;
            case NotEquals:
                filter = d -> !TypeConverterUtils.object2String(d.get(fieldName), "").equals(TypeConverterUtils.object2String(filterValue));
                break;
            case Great:
                filter = d -> TypeConverterUtils.object2Double(d.get(fieldName)).compareTo(TypeConverterUtils.object2Double(filterValue)) > 0;
                break;
            case GreateEquals:
                filter = d -> TypeConverterUtils.object2Double(d.get(fieldName)).compareTo(TypeConverterUtils.object2Double(filterValue)) >= 0;
                break;
            case Less:
                filter = d -> TypeConverterUtils.object2Double(d.get(fieldName)).compareTo(TypeConverterUtils.object2Double(filterValue)) < 0;
                break;
            case LessEquals:
                filter = d -> TypeConverterUtils.object2Double(d.get(fieldName)).compareTo(TypeConverterUtils.object2Double(filterValue)) <= 0;
                break;
            case In:
                filter = d -> TypeConverterUtils.object2String(filterValue, "").contains(TypeConverterUtils.object2String(d.get(fieldName)));
                break;
            case NotIn:
                filter = d -> !TypeConverterUtils.object2String(filterValue, "").contains(TypeConverterUtils.object2String(d.get(fieldName)));
                break;
            case IsNull:
                filter = d -> StringUtils.isEmpty(d.get(fieldName));
                break;
            case IsNotNull:
                filter = d -> !StringUtils.isEmpty(d.get(fieldName));
                break;
            default:
                filter = null;
        }

        return filter;
    }
}
