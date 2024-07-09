package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataAccessCacheTest {

    @Autowired
    DataAccess dataAccess;

    @Test
    void testQueryData() {
        DbCollection coll = dataAccess.queryById("ss_page", 675446015913889792L);
        System.out.println(JSON.toJSONString(coll.getData().get(0)));

          coll = dataAccess.queryById("ss_page", 667857560463740928L);
        System.out.println(JSON.toJSONString(coll.getData().get(0)));
    }
}
