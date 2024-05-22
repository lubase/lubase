package com.lcp.core;

import com.alibaba.fastjson.JSON;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.SqlEntity;
import com.lcp.core.service.DataAccess;
import com.lcp.core.service.update.GenerateUpdateSql;
import com.lcp.coremodel.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GenerateUpdateSqlTest {

    @Autowired
    GenerateUpdateSql generateUpdateSql;

    @Autowired
    DataAccess dataAccess;

    @Test
    void testUpdateDateTimeField() {
        //DbEntity entity, DbTable tableInfo, Boolean isServer, SqlEntity sqlEntity
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setFixField("coldate20,coldate21");
        queryOption.setTableFilter(new TableFilter("ID", "203"));
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 1;
        DbEntity entity = collection.getData().get(0);
        System.out.println(JSON.toJSONString(entity));
        entity.put("coldate20", "2022-02-22");
        entity.put("coldate21", "2022-02-22 12:12:12");

        SqlEntity sqlEntity = new SqlEntity();
        generateUpdateSql.updateSql(entity, collection.getTableInfo(), true, sqlEntity);
        System.out.println(sqlEntity.getSqlStr());
        System.out.println(JSON.toJSONString(sqlEntity));
    }
}