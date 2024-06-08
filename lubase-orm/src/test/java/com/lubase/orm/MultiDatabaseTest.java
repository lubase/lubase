package com.lubase.orm;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.auto.DmDatabaseEntity;
import com.lubase.orm.mapper.MultiDatabaseMapper;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.multiDataSource.DatabaseConnectBuilder;
import com.lubase.orm.multiDataSource.DatabaseConnectModel;
import com.lubase.orm.multiDataSource.DynamicDataSource;
import com.lubase.orm.service.DataAccess;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MultiDatabaseTest {

    @Autowired
    MultiDatabaseMapper multiDatabaseMapper;

    @Autowired
    DynamicDataSource dynamicDataSource;

    @Autowired
    DataAccess dataAccess;

    @Test
    void testGetMainDatabase() {
        DBContextHolder.setMainDataSourceCode();
        String defaultDBName = multiDatabaseMapper.getCurrentDatabaseName();
        System.out.println(String.format("defaultDBName is %s", defaultDBName));
        assert defaultDBName.equals("lcp");
    }

    @Autowired
    DatabaseConnectBuilder databaseConnectBuilder;

    @Test
    void testBuilderDatabaseConnect() {
        DmDatabaseEntity dmDatabaseEntity = new DmDatabaseEntity();
        dmDatabaseEntity.setInstance_name("SQLEXPRESS");
        dmDatabaseEntity.setDatabase_type("sqlserver");
        dmDatabaseEntity.setUser_name("sa");
        dmDatabaseEntity.setPassword("1234.com");
        dmDatabaseEntity.setHost("47.108.130.151");
        dmDatabaseEntity.setPort(1433);
        dmDatabaseEntity.setDatabase_name("cpl");
        DatabaseConnectModel database = databaseConnectBuilder.buildConnectModel(dmDatabaseEntity);

        System.out.println(database);
    }

    @Test
    void testChangeDatabase() {
        DBContextHolder.setDataSourceCode("gwcar1012");
        System.out.println(String.format("defaultDBName is %s", multiDatabaseMapper.getCurrentDatabaseName()));
        assert "gwcar1012".equals(multiDatabaseMapper.getCurrentDatabaseName());

        DBContextHolder.setDataSourceCode("cpl");
        System.out.println(String.format("defaultDBName is %s", multiDatabaseMapper.getCurrentDatabaseName()));
        assert "cpl".equals(multiDatabaseMapper.getCurrentDatabaseName());

        DBContextHolder.setMainDataSourceCode();
        System.out.println(String.format("defaultDBName is %s", multiDatabaseMapper.getCurrentDatabaseName()));
        assert "lcp".equals(multiDatabaseMapper.getCurrentDatabaseName());
    }

    @SneakyThrows
    @Test
    void testAddSalveDatabase() {
        DatabaseConnectModel database = new DatabaseConnectModel();
        database.setAliasCode("cpl");
        database.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        database.setUrl(String.format("jdbc:sqlserver://47.108.130.151:1433;DataBasename=%s;instanceName=SQLEXPRESS", "cpl"));
        database.setUserName("sa");
        database.setPassword("1234.com");
        database.setTestSql("select 1");
        Boolean result = dynamicDataSource.createDataSourceWithCheck(database);
        assert result;
    }

    @SneakyThrows
    @Test
    void testAddSalveDBByPrimaryDBSetting() {
        DBContextHolder.setMainDataSourceCode();
        List<DmDatabaseEntity> coll = multiDatabaseMapper.getAllDatabaseSetting();
        assert coll.size() == 2;
        for (DmDatabaseEntity dmDatabaseEntity : coll) {
            DatabaseConnectModel database = databaseConnectBuilder.buildConnectModel(dmDatabaseEntity);
            System.out.println(database);
            assert dynamicDataSource.createDataSourceWithCheck(database);
        }
    }

    @Test
    void testQuerySlaveDB() {
        QueryOption queryOption = new QueryOption("cp_ecu_main");
        DbCollection coll = dataAccess.queryAllData(queryOption);
        System.out.println(coll.getData().size());

    }
}
