package com.lcp.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTest {
    @Autowired
    RestTemplate restTemplate;

    @Test
    void loadWeb() {
        String url = "http://47.108.130.151:8088/registerColumnInfo/initTableInfoByTableCode?tableCode=form_test";
        String body = restTemplate.getForObject(url, String.class);
        System.out.println(body);
    }
}
