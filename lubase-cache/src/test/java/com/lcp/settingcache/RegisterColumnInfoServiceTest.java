package com.lcp.settingcache;


import com.alibaba.fastjson.JSON;
import com.lubase.model.DbTable;
import com.lubase.cache.service.RegisterColumnInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegisterColumnInfoServiceTest {

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Test
    void testLoadCache() {
        DbTable table = registerColumnInfoService.initTableInfoByTableCode("sa_account");
        System.out.println(JSON.toJSONString(table));

    }
}
