package com.lcp.core;

import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MySqlTest {
    @Autowired
    DataAccess dataAccess;

    @Test
    void testQuery() {
        QueryOption queryOption = new QueryOption("bj_student");
        dataAccess.query(queryOption);
    }

    @Test
    void testLookup() {
        QueryOption queryOption = new QueryOption("bj_demo");
        queryOption.setFixField("nianji.create_time");
        dataAccess.query(queryOption);

        queryOption = new QueryOption("sa_account");
        queryOption.setFixField("organization_id.org_name");
        dataAccess.query(queryOption);
    }

    @Test
    void testUpdate() {
        QueryOption queryOption = new QueryOption("bj_demo");
        DbCollection collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            entity.put("name", "name1");
        }
        dataAccess.update(collection);
    }
}
