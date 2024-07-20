package com.lubase.wfengine;

 
import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WfengineApplicationTests {


    @Autowired
    DataAccess dataAccess;
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Test
    void contextLoads() {
        System.out.println("test");

        QueryOption queryOption = new QueryOption("dm_code");
        DbCollection collection = dataAccess.query(queryOption);

        System.out.println(collection.getData().size());
    }

    @Test
    void  test1(){
        String msg = "hello lubase";
        try {
            String msgPath = String.format("%s:%s", "abc", "007");
            Map<String, Object> headers = new HashMap<>();
            headers.put("KEYS", "001");
            rocketMQTemplate.convertAndSend(msgPath, msg, headers);
        } catch (Exception exception) {
            log.error("流程引擎：流程事件发送MQ发生错误：" + msg, exception);
        }
    }
}
