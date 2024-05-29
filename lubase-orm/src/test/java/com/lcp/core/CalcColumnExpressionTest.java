package com.lcp.core;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CalcColumnExpressionTest {

    @Autowired
    DataAccess dataAccess;

    @Test
    void testCaclColumn() {
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setTableFilter(new TableFilter("id", "639191798853406720"));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        collection.getData().get(0).put("colvarchar10", LocalDateTime.now().toString());
        dataAccess.update(collection);
    }
}
