package com.lcp.qibao;

import com.lubase.core.service.DataAccess;
import com.lubase.core.QueryOption;
import com.lubase.core.constant.CacheConst;
import com.lubase.core.model.DbCollection;
import com.lubase.core.TableFilter;
import com.lubase.core.service.RegisterColumnInfoService;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TableStructCacheTest {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    CacheManager cacheManager;

    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoServiceApi;

    @Qualifier("registerColumnInfoServiceDb")
    @Autowired
    RegisterColumnInfoService registerColumnInfoServiceDb;

    @SneakyThrows
    @Test
    void testColumnCache() {
        Long cache_id = 702563760899887104L;
        DbField field = dataAccess.getDbFieldByColumnId(cache_id);
        String oldName = field.getName();

        QueryOption queryOption = new QueryOption("dm_column");
        queryOption.setTableFilter(new TableFilter("id", "702563760899887104"));
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getData().size() == 1;
        DbEntity entity = collection.getData().get(0);
        String newName = LocalDateTime.now().toString();
        entity.put("col_name", newName);
        Integer rowCount = dataAccess.update(collection);
        assert rowCount == 1;
        Thread.currentThread().join(4000);
        DbField newField = dataAccess.getDbFieldByColumnId(cache_id);
        DbField fieldFromCache = (DbField) cacheManager.getCache("tableStruct").get(CacheConst.PRE_CACHE_COLUMN + cache_id);
        System.out.println(String.format("oldName %s,newName %s ,newField %s ", oldName, newName, newField.getName()));
        assert newName.equals(newField.getName());

    }
}
