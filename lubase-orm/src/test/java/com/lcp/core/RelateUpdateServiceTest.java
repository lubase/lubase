package com.lcp.core;

import com.lubase.orm.model.RelateUpdateModel;
import com.lubase.orm.service.update.RelateUpdateService;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.model.DbTable;
import com.lubase.model.EDBEntityState;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RelateUpdateServiceTest {

    @Autowired
    RelateUpdateService relateUpdateService;

    @Autowired
    RelateUpdateService relateUpdateTableTrigger;

    @Autowired
    DataAccess dataAccess;

    @Test
    void testGetRelateRuleList() {
        List<DbEntity> list = relateUpdateService.getRelateRuleList("679071892711149568");
        assert list.size() == 1;
        System.out.println(list.get(0));
    }

    @Test
    void testGetWriteRule() {
        List<DbEntity> list = relateUpdateService.getRelateRuleList("679071892711149568");
        assert list.size() == 1;

        List<RelateUpdateModel> relateUpdateModels = relateUpdateService.getWriteRule(list.get(0));
        assert relateUpdateModels.size() == 1;
        System.out.println(relateUpdateModels);
    }

    @Test
    void testBuildTargetEntity() {
        List<DbEntity> list = relateUpdateService.getRelateRuleList("679071892711149568");
        assert list.size() == 1;

        List<RelateUpdateModel> relateUpdateModels = relateUpdateService.getWriteRule(list.get(0));
        assert relateUpdateModels.size() > 0;

        DbCollection targetCollection = dataAccess.getEmptyData("cpt_main_task");
        DbCollection currentData = dataAccess.queryById("cpm_project", 686775255238184960L);
        assert currentData.getTotalCount() == 1;
        DbEntity targetEntity = targetCollection.newEntity();
        relateUpdateService.buildTargetEntity(targetEntity, currentData.getData().get(0), relateUpdateModels);
        assert targetEntity.containsKey("task_source");
        assert targetEntity.get("task_source").toString().equals(currentData.getData().get(0).get("pro_name").toString());
    }

    @Test
    void testGetOldTargetData() {
        DbCollection collection = relateUpdateService.getOldTargetData(679613636607479808L, 679726960275886080L, 686775255238184960L);
        assert collection.getTotalCount() == 0;
    }


    @SneakyThrows
    @Test
    void testAddData() {
        DbCollection currentData = dataAccess.queryById("cpm_project", 686775255238184960L);
        Boolean isServer = false;
        DbEntity entity = currentData.getData().get(0);
        entity.setState(EDBEntityState.Added);
        DbTable tableInfo = currentData.getTableInfo();

    }

    @SneakyThrows
    @Test
    void testUpdateData() {
        DbCollection currentData = dataAccess.queryById("cpm_project", 686775311810957312L);
        Boolean isServer = false;
        DbEntity entity = currentData.getData().get(0);
        entity.setState(EDBEntityState.Modified);
        DbTable tableInfo = currentData.getTableInfo();
        entity.put("pro_name", "aaaaa");
    }


}
