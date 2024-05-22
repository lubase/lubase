package com.lcp.core.util;

import com.lcp.core.TableFilter;
import com.lcp.core.operate.EOperateMode;

import java.util.ArrayList;
import java.util.List;

/**
 * TableFiler 包装类
 *
 * @author A
 */
public class TableFilterWrapper {

    TableFilter filter = null;
    List<TableFilter> filters = null;

    public TableFilterWrapper(boolean isAnd) {
        filter = new TableFilter();
        filter.setAnd(isAnd);
        filters = new ArrayList<>();
        filter.setChildFilters(filters);
    }

    public static TableFilterWrapper and() {
        return new TableFilterWrapper(true);
    }

    public static TableFilterWrapper or() {
        return new TableFilterWrapper(false);
    }


    public TableFilter build() {
        if (filters.size() == 0) {
            return null;
        } else if (filters.size() == 1) {
            return filters.get(0);
        } else {
            return filter;
        }
    }

    public TableFilterWrapper addFilter(TableFilter filter) {
        filters.add(filter);
        return this;
    }


    /**
     * 等于 =
     * 例: eq("name", "老王")--->name = '老王'
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper eq(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.Equals));
        return this;
    }

    /**
     * 不等于 <>
     * 例: ne("name", "老王")--->name <> '老王'
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper ne(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.NotEquals));
        return this;
    }

    /**
     * 大于 >
     * 例: gt("age", 18)--->age > 18
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper gt(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.Great));
        return this;
    }

    /**
     * 大于等于 >=
     * 例: ge("age", 18)--->age >= 18
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper ge(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.GreateEquals));
        return this;
    }

    /**
     * 小于等于 <
     * 例: lt("age", 18)--->age < 18
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper lt(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.Less));
        return this;
    }

    /**
     * 小于等于 <=
     * 例: le("age", 18)--->age <= 18
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper le(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.LessEquals));
        return this;
    }

    /**
     * LIKE '%值'
     * 例: likeLeft("name", "王")--->name like '%王'
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper likeRight(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.LikeRight));
        return this;
    }

    /**
     * LIKE '值%'
     * 例: likeRight("name", "王")--->name like '王%'
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper likeLeft(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.LikeLeft));
        return this;
    }

    /**
     * LIKE '%值%'
     * 例: like("name", "王")--->name like '%王%'
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper likeAll(String column, Object val) {
        filters.add(new TableFilter(column, val, EOperateMode.LikeAll));
        return this;
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * 例: in("age",{1,2,3})--->age in (1,2,3)
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper in(String column, String val) {
        filters.add(new TableFilter(column, val, EOperateMode.In));
        return this;
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * 例: notIn("age",{1,2,3})--->age not in (1,2,3)
     *
     * @param column
     * @param val
     */
    public TableFilterWrapper notIn(String column, String val) {
        filters.add(new TableFilter(column, val, EOperateMode.NotIn));
        return this;
    }

    /**
     * 字段 IS NULL
     * 例: isNull("name")--->name is null
     *
     * @param column
     */
    public TableFilterWrapper isNull(String column) {
        filters.add(new TableFilter(column, null, EOperateMode.IsNull));
        return this;
    }

    /**
     * 字段 IS NOT NULL
     * 例: isNotNull("name")--->name is not null
     *
     * @param column
     */
    public TableFilterWrapper isNotNull(String column) {
        filters.add(new TableFilter(column, null, EOperateMode.IsNotNull));
        return this;
    }
}
