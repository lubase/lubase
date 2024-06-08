package com.lubase.orm;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.SsCacheEntity;
import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.service.TableUpdateSettingCacheDataService;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    TableUpdateSettingCacheDataService cacheDataService;


    @Autowired
    TableUpdateSettingCacheDataService tableUpdateSettingCacheDataService;

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


    @Test
    void testGetCacheTable() {
        String tableCode = "dm_table";
        List<SsCacheEntity> list1 = tableUpdateSettingCacheDataService.getTableCacheSettingList().stream().filter(c -> c.getTable_code().equals(tableCode)).collect(Collectors.toList());
        List<SsCacheEntity> list2 = getTableCacheSettingList(tableCode);
        System.out.println(list1);
        System.out.println(list2);
        assert list2.size() == list1.size();
    }

    List<SsCacheEntity> getTableCacheSettingList(String tableCode) {
        return cacheDataService.getTableCacheSettingList()
                .stream().filter(c -> c.getTable_code().equals(tableCode)).collect(Collectors.toList());
    }

}
