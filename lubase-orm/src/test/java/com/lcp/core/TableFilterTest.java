package com.lcp.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.TableFilter;
import com.lubase.orm.operate.EOperateMode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TableFilterTest {


    @Test
    void fromStr() {
        String queryParamsStr = "{\"and\":true,\"not\":false,\"filterName\":\"CODE\",\"filterValue\":\"IP\"," +
                "\"filterDisplay\":\"过滤条件设置\",\"operateMode\":3,\"childFilters\": null}";
        TableFilter filter = JSON.parseObject(queryParamsStr, TableFilter.class);
        assert filter.getOperateMode().equals(EOperateMode.Equals);
        System.out.println(filter);
        System.out.println(JSON.toJSONString(filter));

    }

    @Test
    void fromStr2() {
        String queryParamsStr = "{\"and\":true,\"not\":false,\"filterName\":\"CODE\",\"filterValue\":\"IP\"," +
                "\"filterDisplay\":\"过滤条件设置\",\"operateMode\":\"3\",\"childFilters\": null}";
        TableFilter filter = JSON.parseObject(queryParamsStr, TableFilter.class);
        assert EOperateMode.Equals.equals(filter.getOperateMode());
        System.out.println(filter);
    }

    @Test
    void fromStr3() {
        String queryParamsStr = "{\"FilterDisplay\":\"过滤条件设\",\"FilterTip\":\"\",\"And\":true,\"Not\":false,\"FilterName\":null,\"FilterValue\":null,\"FilterValueNAME\":null,\"OperateMode\":\"3\",\"ChildFilters\":[{\"FilterDisplay\":\"\",\"FilterTip\":\"\",\"And\":true,\"Not\":false,\"FilterName\":\"table_id\",\"FilterValue\":\"slave_table_id\",\"ValueType\":3,\"FilterValueNAME\":null,\"OperateMode\":\"\",\"ChildFilters\":null}]}";
        TableFilter filter = JSON.parseObject(queryParamsStr, TableFilter.class);
        assert EOperateMode.Equals.equals(filter.getOperateMode());
        System.out.println(filter);
    }
}
