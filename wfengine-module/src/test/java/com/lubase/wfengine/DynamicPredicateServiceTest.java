package com.lubase.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.service.DynamicPredicateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootTest
public class DynamicPredicateServiceTest {
    @Autowired
    DataAccess dataAccess;

    @Autowired
    DynamicPredicateService dynamicPredicateService;

    List<DbEntity> getData() {
        QueryOption queryOption = new QueryOption("bs_demo");
        return dataAccess.queryAllData(queryOption).getData();
    }

    @Test
    void test1() {
        List<DbEntity> dataList = getData();
        TableFilterWrapper wrapper = TableFilterWrapper.and();
        wrapper.eq("name", "1");
        Predicate<DbEntity> predicate = dynamicPredicateService.parseTableFilterToPredicate(wrapper.build());
        List<DbEntity> list = dataList.stream().filter(predicate).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(list));
        assert list.size() == 1;

        wrapper = TableFilterWrapper.and();
        wrapper.gt("age", 100);
        predicate = dynamicPredicateService.parseTableFilterToPredicate(wrapper.build());
        list = dataList.stream().filter(predicate).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(list));
        assert list.size() == 2;

        wrapper = TableFilterWrapper.and();
        wrapper.gt("age", 100).likeRight("address", "211");
        predicate = dynamicPredicateService.parseTableFilterToPredicate(wrapper.build());
        list = dataList.stream().filter(predicate).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(list));
        assert list.size() == 1;
    }
}
