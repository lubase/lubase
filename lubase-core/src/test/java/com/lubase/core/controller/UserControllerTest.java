package com.lubase.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Test
    void testLoginError(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        HashMap<String, String> newEntity = new HashMap<>();
        newEntity.put("uid", "admin22");
        newEntity.put("pwd", "88888822");
        HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(newEntity), headers);
        String resultStr = restTemplate.postForObject("/user/login", requestEntity, String.class);
        JSONObject obj = JSON.parseObject(resultStr, JSONObject.class);
        System.out.println(obj);
        assert obj.containsKey("success");
        assert obj.get("success").toString().equals("0");
        assert obj.get("code").toString().equals("401");
        assert obj.get("message").toString().equals("用户名或密码错误");
    }

    @Test
    void testLoginSuccess(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        HashMap<String, String> newEntity = new HashMap<>();
        newEntity.put("uid", "admin");
        newEntity.put("pwd", "888888");
        HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(newEntity), headers);
        String resultStr = restTemplate.postForObject("/user/login", requestEntity, String.class);
        JSONObject obj = JSON.parseObject(resultStr, JSONObject.class);
        System.out.println(obj);
        assert obj.containsKey("success");
        assert obj.get("success").toString().equals("1");

    }
}