package com.lcp.core;

import com.lcp.core.service.IDGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DataIdGeneratorTest {
    @Autowired
    IDGenerator idGenerator;

    @Test
    void getMulIds(){
        List<Long> longs = idGenerator.nextIds(10);
        longs.forEach(m-> System.out.println(m));
        long start = System.currentTimeMillis();
        longs = idGenerator.nextIds(4000);
        long end = System.currentTimeMillis();
        System.out.println(String.format ("用时：%ss,%sms",(end-start)/1000,(end-start)%1000) );
        longs.forEach(m-> System.out.println(m));
    }
}
