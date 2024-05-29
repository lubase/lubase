package com.lcp.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.extend.ITableTrigger;
import com.lubase.orm.extend.service.TableTriggerAdapter;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TableTriggerServiceTest {

    @Autowired
    TableTriggerAdapter tableTriggerAdapter;

    @Autowired
    DataAccess dataAccess;

    @Autowired
    List<ITableTrigger> tableTriggerList;

    @Test
    void testTableTrigger() {
        System.out.println(tableTriggerList);
        DbTable table = dataAccess.initTableInfoByTableCode("md_price");
        System.out.println(JSON.toJSONString(table));
        List<ITableTrigger> triggerList = tableTriggerAdapter.getTableTriggerList(table);

        System.out.println(JSON.toJSONString(triggerList));
        assert triggerList.size() == 1;
    }
}
