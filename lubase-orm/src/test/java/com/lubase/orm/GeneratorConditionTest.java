package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.QueryOptionWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratorConditionTest {

    @Test
    void testAnd() {
        TableFilter filter = TableFilterWrapper.and().eq("col1", "value").eq("col2", "value2").build();
        System.out.println(JSON.toJSONString(filter));
    }

    @Test
    void testOr() {
        TableFilter filter = TableFilterWrapper.or().eq("col1", "value").eq("col2", "value2").build();
        System.out.println(JSON.toJSONString(filter));
    }

    @Test
    void test1() {
        TableFilter filter1 = TableFilterWrapper.and().eq("col1", "value").eq("col2", "value2").build();
        TableFilter filter2 = TableFilterWrapper.or().eq("col2", "val3").addFilter(filter1).build();
        System.out.println(JSON.toJSONString(filter2));
    }

    @Test
    void test2() {
        TableFilter filter1 = TableFilterWrapper.and().eq("col1", "value").eq("col2", "value2").build();
        TableFilter filter2 = TableFilterWrapper.or().eq("col2", "val3").addFilter(filter1).build();
        QueryOption queryOption = QueryOptionWrapper.select("*").from("sscode").where(filter2).build();
        System.out.println(JSON.toJSONString(queryOption));
    }

    @Test
    void testOneFilter() {
        assert TableFilterWrapper.and().build() == null;
        TableFilter filter1 = TableFilterWrapper.and().eq("col1", "value1").build();
        TableFilterWrapper.or().build();
        System.out.println(JSON.toJSONString(filter1));
    }
}
