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
            entity2.put("data_id", entity.getId());
            entity2.put("field", "page_name");
            entity2.put("user_language", "en-US");
            entity2.put("msg", "en_" + entity.get("page_name"));
            entity2.setState(EDBEntityState.Added);
            coll2.getData().add(entity2);
        }
        dataAccess.update(coll2);
    }

}
