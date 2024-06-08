package com.lubase.orm;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.time.LocalDateTime;

@EnableCaching
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TransactionalTest {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    void testGetEmptyData() {
        System.out.println("00000000");
        dataAccess.getEmptyDataByTableId(769256403964530688L);
        System.out.println("11111111");
        dataAccess.getEmptyDataByTableId(2022052817099649644L);
        System.out.println("22222222");
        dataAccess.getEmptyDataByTableId(769256403964530688L);
        System.out.println("33333333333");
    }

    @Test
    void testMainDbDemo() {
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setTableFilter(new TableFilter("id", "639191178482290688", EOperateMode.Equals));
        DbCollection coll = dataAccess.query(queryOption);
        assert coll.getData().size() == 1;

       // TransactionDefinition definition = new DefaultTransactionDefinition();
        //transactionManager.set
       // TransactionStatus trans1 = transactionManager.getTransaction(definition);
        try {
            DbEntity oldEntity = coll.getData().get(0);
            oldEntity.put("colvarchar10", "待办" + LocalDateTime.now().toString().substring(10));
            dataAccess.update(coll);

            DbEntity newEntity = coll.newEntity();
            newEntity.put("colvarchar10", "3");
            newEntity.put("colvarchar11", "3");
            newEntity.put("id", "204");
            coll.getData().clear();
            coll.getData().add(newEntity);
            int rowCount = dataAccess.update(coll);
      //      transactionManager.commit(trans1);
        } catch (Exception e) {
            e.printStackTrace();
       //     transactionManager.rollback(trans1);
        } finally {
            System.out.println("执行完毕");
        }
    }

    @Test
    void testSimpleDemo() throws SQLException {
        QueryOption queryOption = new QueryOption("cpt_tab_title");
        queryOption.setTableFilter(new TableFilter("id", 705149394235691008L, EOperateMode.Equals));
        DbCollection coll = dataAccess.query(queryOption);
        assert coll.getData().size() == 1;

        DBContextHolder.setDataSourceCode("649406366640771072");
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus trans1 = transactionManager.getTransaction(definition);

        try {

            DbEntity oldEntity = coll.getData().get(0);
            oldEntity.put("title_name", "待办" + LocalDateTime.now());
            int rowCount = dataAccess.update(coll);

            queryMain();

            DbEntity newEntity = coll.newEntity();
            newEntity.put("title_code", "3");
            newEntity.put("title_name", '3');
            newEntity.put("id", "705149394235691004");
            coll.getData().clear();
            coll.getData().add(newEntity);

            rowCount += dataAccess.update(coll);
            //transactionManager.rollback(trans1);
            transactionManager.commit(trans1);
            System.out.println("rowCount is " + rowCount);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(trans1);
        } finally {
            System.out.println("执行完毕");
        }
    }

    void queryMain() {
        QueryOption queryOption = new QueryOption("dm_code");
        dataAccess.queryAllData(queryOption);
    }
}
