package com.lubase.orm;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.impl.registerColumnInfoServiceApiImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class registerColumnInfoServiceApiImplTest {

    @Autowired
    registerColumnInfoServiceApiImpl apiService;

    @Test
    void test1() {
        System.out.println(LocalDateTime.now().toLocalTime());
        System.out.println(apiService.initTableInfoByTableId(2022052817099649650L));
        System.out.println(apiService.initTableInfoByTableCode("dm_column"));
        System.out.println(apiService.getTableIdByTableCode("dm_column"));
        System.out.println(apiService.getTableCodeByTableId(2022052817099649650L));
        System.out.println(apiService.getColumnsByTableId(2022052817099649650L));
        System.out.println(apiService.getColumnInfoByColumnId(2022052821007066246L));
        System.out.println(LocalDateTime.now().toLocalTime());
        System.out.println(apiService.initTableInfoByTableId(2022052817099649650L));
        System.out.println(apiService.initTableInfoByTableCode("dm_column"));
        System.out.println(apiService.getTableIdByTableCode("dm_column"));
        System.out.println(apiService.getTableCodeByTableId(2022052817099649650L));
        System.out.println(apiService.getColumnsByTableId(2022052817099649650L));
        System.out.println(apiService.getColumnInfoByColumnId(2022052821007066246L));
        System.out.println(LocalDateTime.now().toLocalTime());
    }

    @Autowired
    DataAccess dataAccess;

    @Test
    void testGetFile() {
        QueryOption queryOption = new QueryOption("cpt_task_list");
        queryOption.setFixField("main_task_id.file_key");
        queryOption.setTableFilter(new TableFilter("id", "948647756942020608"));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        System.out.println(collection.getData());

        queryOption = new QueryOption("cpt_task_list");
        queryOption.setFixField("main_task_id,main_task_id.file_key");
        queryOption.setTableFilter(new TableFilter("id", "948647756942020608"));
        collection = dataAccess.queryAllData(queryOption);
        System.out.println(collection.getData());

    }
}
