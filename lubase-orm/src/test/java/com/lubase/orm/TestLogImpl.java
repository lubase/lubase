package com.lubase.orm;

import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestLogImpl {


    @Autowired
    DataAccess dataAccess;

    @Test
    void test1() {
        log.info("info lubase");
        log.warn("warn lubase");
        log.error("error lubase");

        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setTableFilter(new TableFilter("user_code", "admin1"));
        DbCollection coll = dataAccess.query(queryOption);
        log.info("user_code is admin1 data rows is " + coll.getData().size());
    }

}
