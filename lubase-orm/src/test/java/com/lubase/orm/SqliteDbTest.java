package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.model.DbEntity;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.codec.DataBufferEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SqliteDbTest {

    @Autowired
    DataAccess dataAccess;

    @Test
    public void contextLoads() {
        QueryOption queryOption = new QueryOption("a33_abc");
        DbCollection coll = dataAccess.queryAllData(queryOption);
        System.out.println(JSON.toJSONString(coll.getData()));

        DbEntity entity = coll.newEntity();
        entity.put("aa", "test");
        coll.getData().add(entity);
        dataAccess.update(coll);
    }
}
