package com.lcp.wfengine;


import com.alibaba.fastjson.JSON;
import com.lubase.core.model.DbCollection;
import com.lubase.wfengine.remote.RemoteBisDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RemoteBisDataServiceTest {

    @Autowired
    RemoteBisDataService remoteBisDataService;

    @Test
    void test1() {
        String serverId = "769343035275218944";
        String dataId = "942886439987712000";

        DbCollection coll = remoteBisDataService.getBisData(serverId, dataId);

        System.out.println(JSON.toJSONString(coll.getData().get(0)));
    }

}
