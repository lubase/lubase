package com.lubase.wfengine;

 
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WfengineApplicationTests {


    @Autowired
    DataAccess dataAccess;

    @Test
    void contextLoads() {
        System.out.println("test");

        QueryOption queryOption = new QueryOption("dm_code");
        DbCollection collection = dataAccess.query(queryOption);

        System.out.println(collection.getData().size());
    }

}
