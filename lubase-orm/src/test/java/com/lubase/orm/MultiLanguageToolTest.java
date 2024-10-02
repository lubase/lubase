package com.lubase.orm;

import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LubaseOrmApplication.class)
public class MultiLanguageToolTest {
    @Autowired
    DataAccess dataAccess;

    @Test
    void test() {
        QueryOption queryOption = new QueryOption("ss_page");
        queryOption.setTableFilter(new TableFilter("app_id", "671085014334574592"));
        DbCollection coll = dataAccess.queryAllData(queryOption);

        DbCollection coll2 = dataAccess.getEmptyData("ss_resource");
        for (DbEntity entity : coll.getData()) {
            DbEntity entity2 = coll2.newEntity();
            entity2.put("app_id", entity.get("app_id"));
            entity2.put("table_id", "2022052817099649658");
            entity2.put("table_code", "ss_page");
            entity2.put("data_id", entity.getId());
            entity2.put("field", "page_name");
            entity2.put("user_language", "en_US");
            entity2.put("msg", "en_" + entity.get("page_name"));
            entity2.setState(EDBEntityState.Added);
            coll2.getData().add(entity2);
        }
        dataAccess.update(coll2);
    }

    @Test
    void testButton() {
        QueryOption queryOption = new QueryOption("ss_button");
        queryOption.setTableFilter(new TableFilter("page_id.app_id", "671085014334574592"));
        DbCollection coll = dataAccess.queryAllData(queryOption);

        DbCollection coll2 = dataAccess.getEmptyData("ss_resource");
        for (DbEntity entity : coll.getData()) {
            DbEntity entity2 = coll2.newEntity();
            entity2.put("app_id", "671085014334574592");
            entity2.put("table_id", "2022052817099649653");
            entity2.put("table_code", "ss_button");
            entity2.put("data_id", entity.getId());
            entity2.put("field", "button_name");
            entity2.put("user_language", "en_US");
            entity2.put("msg", "en_" + entity.get("button_name"));
            entity2.setState(EDBEntityState.Added);
            coll2.getData().add(entity2);
        }
        dataAccess.update(coll2);
    }

    @Test
    void testButton2() {
        QueryOption queryOption = new QueryOption("ss_button");
        queryOption.setTableFilter(new TableFilter("page_id.app_id", "671085014334574592"));
        DbCollection coll = dataAccess.queryAllData(queryOption);

        DbCollection coll2 = dataAccess.getEmptyData("ss_resource");
        for (DbEntity entity : coll.getData()) {
            if (entity.get("group_des") == null || entity.get("group_des").toString().isEmpty()) {
                continue;
            }
            DbEntity entity2 = coll2.newEntity();
            entity2.put("app_id", "671085014334574592");
            entity2.put("table_id", "2022052817099649653");
            entity2.put("table_code", "ss_button");
            entity2.put("data_id", entity.getId());
            entity2.put("field", "group_des");
            entity2.put("user_language", "en_US");
            entity2.put("msg", "en_" + entity.get("group_des"));
            entity2.setState(EDBEntityState.Added);
            coll2.getData().add(entity2);
        }
        dataAccess.update(coll2);
    }

    @Test
    void testDMCOLUMN() {
        QueryOption queryOption = new QueryOption("dm_column");
        queryOption.setTableFilter(new TableFilter("table_id.app_id", "671085014334574592"));
        DbCollection coll = dataAccess.queryAllData(queryOption);

        DbCollection coll2 = dataAccess.getEmptyData("ss_resource");
        for (DbEntity entity : coll.getData()) {
            DbEntity entity2 = coll2.newEntity();
            entity2.put("app_id", "671085014334574592");
            entity2.put("table_id", "2022052817099649646");
            entity2.put("table_code", "dm_column");
            entity2.put("data_id", entity.getId());
            entity2.put("field", "col_name");
            entity2.put("user_language", "en_US");
            entity2.put("msg", "en_" + entity.get("col_name"));
            entity2.setState(EDBEntityState.Added);
            coll2.getData().add(entity2);
        }
        dataAccess.update(coll2);
    }
}
