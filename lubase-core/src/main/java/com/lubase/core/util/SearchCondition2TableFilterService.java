package com.lubase.core.util;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.core.model.ESearchConditionType;
import com.lubase.core.model.SearchCondition;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class SearchCondition2TableFilterService {

    @SneakyThrows
    public TableFilter convertToTableFilter(String searchParamStr) {
        if (StringUtils.isEmpty(searchParamStr)) {
            return null;
        }
        try {
            List<SearchCondition> list = JSON.parseArray(searchParamStr, SearchCondition.class);
            return convertToTableFilter(list);
        } catch (Exception e) {
            throw new WarnCommonException("convertToTableFilter失败：" + searchParamStr);
        }
    }

    public TableFilter convertToTableFilter(List<SearchCondition> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        for (SearchCondition condition : list) {
            String searchValue = getSearchValue(condition);
            if (StringUtils.isEmpty(searchValue)) {
                continue;
            }
            if (condition.getFilterType().equals(ESearchConditionType.LikeAll.getType())) {
                if (condition.getColumnCode().contains(",")) {
                    TableFilterWrapper filterWrapper1 = TableFilterWrapper.or();
                    for (String col : condition.getColumnCode().split(",")) {
                        filterWrapper1.likeAll(col, searchValue);
                    }
                    filterWrapper.addFilter(filterWrapper1.build());
                } else {
                    filterWrapper.likeAll(condition.getColumnCode(), searchValue);
                }
            } else if (condition.getFilterType().equals(ESearchConditionType.Equal.getType())) {
                if (condition.getColumnCode().contains(",")) {
                    TableFilterWrapper filterWrapper1 = TableFilterWrapper.or();
                    for (String col : condition.getColumnCode().split(",")) {
                        filterWrapper1.eq(col, searchValue);
                    }
                    filterWrapper.addFilter(filterWrapper1.build());
                } else {
                    filterWrapper.eq(condition.getColumnCode(), searchValue);
                }
            } else if (condition.getFilterType().equals(ESearchConditionType.LikeLeft.getType())) {
                if (condition.getColumnCode().contains(",")) {
                    TableFilterWrapper filterWrapper1 = TableFilterWrapper.or();
                    for (String col : condition.getColumnCode().split(",")) {
                        filterWrapper1.likeLeft(col, searchValue);
                    }
                    filterWrapper.addFilter(filterWrapper1.build());
                } else {
                    filterWrapper.likeLeft(condition.getColumnCode(), searchValue);
                }
            } else if (condition.getFilterType().equals(ESearchConditionType.LikeRight.getType())) {
                if (condition.getColumnCode().contains(",")) {
                    TableFilterWrapper filterWrapper1 = TableFilterWrapper.or();
                    for (String col : condition.getColumnCode().split(",")) {
                        filterWrapper1.likeRight(col, searchValue);
                    }
                    filterWrapper.addFilter(filterWrapper1.build());
                } else {
                    filterWrapper.likeRight(condition.getColumnCode(), searchValue);
                }
            } else if (condition.getFilterType().equals(ESearchConditionType.Range.getType())) {
                String[] rangeValues = searchValue.split(",");
                if (rangeValues.length != 2 || ",".equals(condition.getValue())) {
                    continue;
                }
                TableFilter leftFilter = new TableFilter(condition.getColumnCode(), rangeValues[0], EOperateMode.GreateEquals);
                TableFilter rightFilter = new TableFilter(condition.getColumnCode(), rangeValues[1], EOperateMode.LessEquals);
                filterWrapper.addFilter(leftFilter).addFilter(rightFilter);
            } else if (condition.getFilterType().equals(ESearchConditionType.AllValue.getType())) {
                for (String tmpValue : searchValue.split(",")) {
                    if (StringUtils.isEmpty(tmpValue)) {
                        continue;
                    }
                    filterWrapper.likeAll(condition.getColumnCode(), tmpValue);
                }
            } else if (condition.getFilterType().equals(ESearchConditionType.AnyValue.getType())) {
                TableFilterWrapper filterWrapper1 = TableFilterWrapper.or();
                for (String tmpValue : searchValue.split(",")) {
                    if (StringUtils.isEmpty(tmpValue)) {
                        continue;
                    }
                    filterWrapper1.likeAll(condition.getColumnCode(), tmpValue);
                }
                filterWrapper.addFilter(filterWrapper1.build());
            }
        }
        return filterWrapper.build();
    }

    private String getSearchValue(SearchCondition condition) {
        //TODO:是否替换宏变量
        return condition.getValue();
    }
}
