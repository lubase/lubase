package com.lubase.wfengine;

import com.lubase.wfengine.config.EngineConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EngineConfigTest {
    @Autowired
    EngineConfig engineConfig;

    @Test
    void test() {
        System.out.println("……");

        System.out.println("……");
    }
}
