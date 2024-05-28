package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.lubase.starter.extend.IFormTrigger;
import com.lubase.starter.extend.service.CustomFormServiceAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomFormServiceAdapterTest {

    @Autowired
    CustomFormServiceAdapter customFormServiceAdapter;

    @Test
    void testLoadAllTrigger() {
        List<IFormTrigger> triggers = customFormServiceAdapter.getAllFormTrigger();
        System.out.println(JSON.toJSON(triggers));
        assert triggers.size() == 2;
    }

    @Test
    void testLoadByPath() {
        String path = "com.lcp.qibao.formtrigger.OrgFormTrigger";
        IFormTrigger trigger = customFormServiceAdapter.getFormTriggerByPath(path);
        assert trigger != null;
        System.out.println(JSON.toJSONString(trigger));
    }
}
