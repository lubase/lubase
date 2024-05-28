package com.lcp.core;

import com.lubase.core.model.SqlEntity;
import com.lubase.core.service.DataAccess;
import com.lubase.core.service.update.GenerateUpdateSql;
import com.lubase.model.DbTable;
import com.lubase.core.service.RegisterColumnInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootTest
@EnableCaching
public class SqlEntityTest {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;
    GenerateUpdateSql generateUpdateSql = new GenerateUpdateSql();

    @Test
    void testNew() {
        SqlEntity entity = new SqlEntity();
        System.out.println(entity.toString());
        entity.addParam("value1");
        System.out.println(entity.toString());
        entity.addParam(2);
        System.out.println(entity.toString());
    }


    @Test
    void testDeleteSql() {
        DbTable dbTable = dataAccess.initTableInfoByTableCode("form_test");
//        DbEntity suuser = suuserMapper.selectUserById("00000000");
//        System.out.println(suuser);
//        SqlEntity sqlEntity = new SqlEntity();
//        generateUpdateSql.deleteSql(suuser, dbTable, sqlEntity);
//        System.out.println(sqlEntity);
    }

    @Test
    void testEditSql() {
        DbTable dbTable = dataAccess.initTableInfoByTableCode("form_test");
//        DbEntity suuser = suuserMapper.selectUserById("00000000");
//        System.out.println(suuser);
//        SqlEntity sqlEntity = new SqlEntity();
//        generateUpdateSql.updateSql(suuser, dbTable, true, sqlEntity);
//        System.out.println(sqlEntity);
    }

    @Test
    void testAddSql() {
        DbTable dbTable = dataAccess.initTableInfoByTableCode("form_test");
        System.out.println("^^^^^^^^^^^^^^^");
        dbTable = dataAccess.initTableInfoByTableCode("form_test");
//        DbEntity suuser = suuserMapper.selectUserById("00000000");
//        System.out.println(suuser);
//        SqlEntity sqlEntity = new SqlEntity();
//        generateUpdateSql.addSql(suuser, dbTable, sqlEntity, true);
//        System.out.println(sqlEntity);
    }

    @Test
    void testQuery() {

    }
}
