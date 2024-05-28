package com.lcp.wfengine;

 
import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
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
