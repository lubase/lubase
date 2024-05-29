package com.lcp.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.SqlEntity;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.update.DataAccessUpdateCoreService;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataAccessUpdateTest {
    @Autowired
    DataAccess dataAccess;

    @Autowired
    DataAccessUpdateCoreService dataAccessUpdateCoreService;

    @Test
    void testDbEntityUpdate() {
        DbEntity entity = new DbEntity();
        entity.put("col1", "val1");
        System.out.println(entity.getDataState());
        entity.put("col2", "val2");
        System.out.println(entity.getDataState());
        System.out.println(entity);
        boolean isChanged = entity.isPropertyChanged("col1");
        System.out.println(isChanged);
    }

    @Test
    void testEntityAdd() {
        DbEntity newEntity = new DbEntity();
        assert newEntity.getDataState().equals(EDBEntityState.UnChanged);
        newEntity.setState(EDBEntityState.Added);
        newEntity.put("col1", "vale");
        assert newEntity.getDataState().equals(EDBEntityState.Added);
    }


    @Test
    void testAddData() {
        addEntity();
    }

    Long addEntity() {
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setFixField("*");
        DbCollection collection = dataAccess.queryFieldList(queryOption);

        DbEntity newEntity = new DbEntity();
        newEntity.setState(EDBEntityState.Added);
        newEntity.put("colvarchar10", "colvarchar10");
        newEntity.put("colvarchar11", "12312312312");
        collection.setData(new ArrayList<>());
        collection.getData().add(newEntity);

        int count = dataAccess.update(collection);
        System.out.println("count is " + count);
        return newEntity.getId();
    }

    @Test
    void testDeleteData() {
        Long id = addEntity();
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setFixField("*");
        queryOption.getTableFilter().setFilterName("ID");
        queryOption.getTableFilter().setFilterValue(id);
        queryOption.getTableFilter().setOperateMode(EOperateMode.Equals);
        DbCollection collection = dataAccess.query(queryOption);

        collection.getData().get(0).setState(EDBEntityState.Deleted);
        int count = dataAccess.update(collection);
        System.out.println("count is " + count);

        assert dataAccess.query(queryOption).getData().size() == 0;
    }



    @Test
    void testUpdateOneData() {
        Long id = addEntity();
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setFixField("*");
        queryOption.getTableFilter().setFilterName("ID");
        queryOption.getTableFilter().setFilterValue(id);
        queryOption.getTableFilter().setOperateMode(EOperateMode.Equals);
        DbCollection collection = dataAccess.query(queryOption);

        assert collection.getData().size() == 1;
        DbEntity entity = collection.getData().get(0);
        System.out.println("entity is :" + collection.getData().get(0));
        entity.put("colvarchar10", "修改后的值2");
        System.out.println("entity is :" + collection.getData().get(0));
        List<SqlEntity> sqlEntityList = ReflectionTestUtils.invokeMethod(dataAccessUpdateCoreService, "generateUpdateSql", collection);
        System.out.println("…………………………sql is " + sqlEntityList.get(0).getSqlStr());
        System.out.println(sqlEntityList);

        int count = dataAccess.update(collection);
        System.out.println("count is " + count);
    }

    @Test
    void testUpdateDateTimeField() {
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setFixField("COLDATE20,COLDATE21");
        queryOption.setTableFilter(new TableFilter("ID", "203"));
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 1;
        DbEntity entity = collection.getData().get(0);
        System.out.println(JSON.toJSONString(entity));

        entity.put("COLDATE20", LocalDateTime.now());
        entity.put("COLDATE21", LocalDateTime.now());
        assert entity.getDataState().equals(EDBEntityState.Modified);
        Integer count = dataAccess.update(collection);
        assert count == 1;
        collection = dataAccess.query(queryOption);

        System.out.println(JSON.toJSONString(collection.getData().get(0)));
    }
}
