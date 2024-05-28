package com.lubase.core.util;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DbEntityTool {
    public static List<DbEntity> mergeEntityList(List<DbEntity> oldList, List<DbEntity> newList) {
        if (newList == null) {
            newList = new ArrayList<>();
        }
        //原数据设置为不更新状态，匹配新数据，识别新增和修改数据、删除旧数据
        for (DbEntity oldCode : oldList) {
            oldCode.setState(EDBEntityState.UnChanged);
            if (newList.stream().noneMatch(n -> n.getId().equals(oldCode.getId()))) {
                oldCode.setState(EDBEntityState.Deleted);
            }
        }
        for (DbEntity entity : newList) {
            if (oldList.stream().anyMatch(old -> old.getId().equals(entity.getId()))) {
                entity.acceptChange();
                entity.setState(EDBEntityState.Modified);
            } else {
                entity.setState(EDBEntityState.Added);
            }
            oldList.add(entity);
        }
        return oldList;
    }

    public static QueryOption mergeClientQueryOption(QueryOption serverQuery, QueryOption clientQuery) {
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        if (clientQuery != null) {
            serverQuery.setPageIndex(clientQuery.getPageIndex());
            serverQuery.setPageSize(clientQuery.getPageSize());
            if (!StringUtils.isEmpty(clientQuery.getSortField())) {
                serverQuery.setSortField(clientQuery.getSortField());
            } else {
                String sortField = String.format("\"%s\"", serverQuery.getTableName()) + "." + String.format("\"%s\"", "id") + " ASC";
                serverQuery.setSortField(sortField);
            }
            if (tableFilterIsNotNull(clientQuery.getTableFilter())) {
                filterWrapper.addFilter(clientQuery.getTableFilter());
            }
        }
        if (tableFilterIsNotNull(serverQuery.getTableFilter())) {
            filterWrapper.addFilter(serverQuery.getTableFilter());
        }
        serverQuery.setTableFilter(filterWrapper.build());
        return serverQuery;
    }

    public static Boolean tableFilterIsNull(TableFilter filter) {
        if (filter == null) {
            return true;
        }
        if (StringUtils.isEmpty(filter.getFilterName())
                && (filter.getChildFilters() == null || filter.getChildFilters().size() == 0)) {
            return true;
        }
        return false;
    }

    public static Boolean tableFilterIsNotNull(TableFilter filter) {
        return !tableFilterIsNull(filter);
    }
}
