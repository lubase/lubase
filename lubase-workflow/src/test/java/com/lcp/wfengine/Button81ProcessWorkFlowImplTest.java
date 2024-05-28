package com.lcp.wfengine;


import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.starter.auto.entity.SsButtonEntity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class Button81ProcessWorkFlowImplTest {


    @Autowired
    DataAccess dataAccess;

    @SneakyThrows
    @Test
    void testGetOInsByDataId() {
        DbCollection collection = dataAccess.queryById("ss_button", 811662993837264896L);
        List<SsButtonEntity> buttonEntityList = collection.getGenericData(SsButtonEntity.class);
        assert buttonEntityList.size() == 1;
        SsButtonEntity button = buttonEntityList.get(0);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "806191813860790272");
//        Object obj = button81ProcessWorkFlow.exe(button, map);
//        assert obj != null;
//        System.out.println(JSON.toJSONString(obj));
    }
}
