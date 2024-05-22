package com.lcp.core;

import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
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
