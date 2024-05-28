package com.lcp.qibao.basic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StackTest {

    @Test
    void testBatch() {
        int i = 1;
        System.out.println(++i);
    }
}
