package com.lcp.settingcache;

import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@SpringBootTest
class SettingcacheApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(System.getProperty("user.timezone"));

        String key = "12312332_table_code,col_code";
        String dataId = key.split("_")[0];
        String fileKey = key.substring(key.indexOf("_"));

        System.out.println(dataId);
        System.out.println(fileKey);

    }

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void testRedis() {
        System.out.println("11122");
        DbEntity entity = new DbEntity();
        entity.put("id", "123123");
        entity.put("name", "张三");

        redisTemplate.opsForValue().set("testKey", entity);

        DbEntity entity1 = (DbEntity) redisTemplate.opsForValue().get("testKey");
        System.out.println(entity1);
    }


}
