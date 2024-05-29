package com.lcp.core;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataAccessOtherDBQueryTest {
    @Autowired
    DataAccess dataAccess;

    @Test
    void testGetSalveDB() {
        QueryOption queryOption = new QueryOption("ecu_list");
        DbCollection dbCollection = dataAccess.queryAllData(queryOption);

        System.out.println("记录数：" + dbCollection.getData().size());
    }
}
