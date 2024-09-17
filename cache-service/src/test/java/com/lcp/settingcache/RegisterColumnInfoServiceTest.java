package com.lcp.settingcache;


import com.lubase.cache.SettingcacheApplication;
import com.lubase.model.DbTable;
import com.lubase.cache.service.RegisterColumnInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest(classes = SettingcacheApplication.class)
public class RegisterColumnInfoServiceTest {

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    CacheManager cacheManager;

    @Test
    void testLoadCache() {
        DbTable table = registerColumnInfoService.initTableInfoByTableCode("sa_account");
        System.out.println(table);
    }

    @Test
    void testCache() {
        DbTable table = cacheManager.getCache("tableStruct").get("dm:t:dm_code", DbTable.class);
        System.out.println(table);
    }
}
