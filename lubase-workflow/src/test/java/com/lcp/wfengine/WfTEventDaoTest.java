package com.lcp.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.dao.WfTEventDao;
import com.lubase.wfengine.model.EOpenEventType;
import com.lubase.wfengine.model.OpenEventModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WfTEventDaoTest {
    @Autowired
    WfTEventDao wfTEventDao;

    @Test
    void testGetUnProcessEvent() {
        List<WfTEventEntity> list = wfTEventDao.getUnProcessEvent(10);
        System.out.println(JSON.toJSON(list));
    }

    @Test
    void testMsg() {
        OpenEventModel eventModel = new OpenEventModel(EOpenEventType.CompleteTIns, "serviceId", "dataId", "finsid");
        String msg = JSON.toJSONString(eventModel);
        System.out.println(msg);
    }
}
