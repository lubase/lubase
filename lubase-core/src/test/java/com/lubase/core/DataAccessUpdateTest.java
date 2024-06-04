package com.lubase.core;


import com.lubase.core.entity.DmColumnEntity;
import com.lubase.core.entity.DmTableEntity;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.TableFilter;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataAccessUpdateTest {
    @Autowired
    DataAccess dataAccess;

    @Test
    void testDeleteData() {
        Long dataId = 708773055735795712L;
        DbCollection currentData = dataAccess.queryById("cpm_testTL_project_leader", dataId, "id");
        assert currentData.getData().size() == 1;

        QueryOption queryOption = new QueryOption("cpm_contact_person");
        queryOption.setTableFilter(new TableFilter("data_id", dataId.toString()));
        queryOption.setFixField("id");
        DbCollection targetData = dataAccess.queryAllData(queryOption);
        assert targetData.getData().size() == 1;

        DbEntity entity = currentData.getData().get(0);
        entity.setState(EDBEntityState.Deleted);
        dataAccess.update(currentData);

        assert dataAccess.queryById("cpm_testTL_project_leader", dataId).getData().size() == 0;
        assert dataAccess.queryAllData(queryOption).getData().size() == 0;
    }

    @Test
    void testUpdate(){
        String tableId = "2022052817099649651";
        String refCols = "663856657188524033";
        //1、获取表或视图信息
        DbEntity tableEntity = getTableEntity(tableId);
        Boolean isView = TypeConverterUtils.object2Integer(tableEntity.get(DmTableEntity.COL_IS_VIEW)) == 1;
        DbCollection collOldColumn = getOldColumn(tableId);
        //2、获取待添加列信息
        DbCollection collAddColumn = getAddColumn(refCols);
        List<DbEntity> listAdd = new ArrayList<>();
        for (DbEntity entity : collAddColumn.getData()) {
            if (collOldColumn.getData().stream().anyMatch(c -> c.get(DmColumnEntity.COL_COL_CODE).toString().equals(entity.get(DmColumnEntity.COL_COL_CODE).toString()))) {
                continue;
            }
            if (isView) {
                entity.put("_is_view_column", 1);
            }
            entity.setState(EDBEntityState.Added);
            entity.put(DmColumnEntity.COL_TABLE_ID, tableId);
            entity.put(DmColumnEntity.COL_TABLE_CODE, tableEntity.get(DmTableEntity.COL_TABLE_CODE));
            entity.put("id", null);
            listAdd.add(entity);
        }
        //3、为表或者视图增加注册信息
        collAddColumn.setData(listAdd);
        Integer rowsCount = dataAccess.update(collAddColumn);
    }

    @SneakyThrows
    DbEntity getTableEntity(String tableId) {
        QueryOption queryOption = new QueryOption(DmTableEntity.TABLE_CODE);
        queryOption.setFixField("is_view,table_code,database_id");
        queryOption.setTableFilter(new TableFilter("id", tableId));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        if (collection.getData().size() == 0) {
            throw new WarnCommonException("tableId参数错误，请检查");
        }
        return collection.getData().get(0);
    }

    DbCollection getOldColumn(String tableId) {
        QueryOption queryOption = new QueryOption(DmColumnEntity.TABLE_CODE);
        queryOption.setTableFilter(new TableFilter(DmColumnEntity.COL_TABLE_ID, tableId));
        return dataAccess.queryAllData(queryOption);
    }

    DbCollection getAddColumn(String colIds) {
        QueryOption queryOption = new QueryOption(DmColumnEntity.TABLE_CODE);
        TableFilterWrapper wrapper = TableFilterWrapper.or();
        for (String id : StringUtils.split(colIds, ",")) {
            try {
                wrapper.eq("id", Long.parseLong(id));
            } catch (Exception exception) {

            }
        }
        queryOption.setTableFilter(wrapper.build());
        return dataAccess.queryAllData(queryOption);
    }
}
