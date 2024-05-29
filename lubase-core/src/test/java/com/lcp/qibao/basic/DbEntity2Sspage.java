package com.lcp.qibao.basic;

import com.alibaba.fastjson.JSON;
import com.lubase.model.DbEntity;
import com.lubase.core.entity.SsPageEntity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DbEntity2Sspage {
    @SneakyThrows
    @Test
    void test2() {
        Class<?> type = SsPageEntity.class;
        assert DbEntity.class.isAssignableFrom(type);
        System.out.println("1:" + type.getName());
        for (Class<?> type2 : type.getInterfaces()) {
            System.out.println(type2.getName());
        }
        Object o = type.getDeclaredConstructor().newInstance();
        System.out.println("2:" + o.getClass().getName());
        DbEntity entity = (DbEntity) o;
        entity.put("aa", "aaa");
        System.out.println(JSON.toJSONString(entity));
        System.out.println("3:" + entity.getClass().getName());
    }
}
