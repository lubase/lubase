package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.lubase.core.model.InvokeMethodParamMoreDataDTO;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FuncPhysicalDeleteTest {

    @Test
    void testDeleteOneData(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        tmpModel tmpModel = new tmpModel();
        tmpModel.setFuncCode("010209_delete1");
        HashMap<String, String> map = new HashMap<>();
        map.put("ID", "000000WOXVH2W0000A0S");
        tmpModel.setData(map);
        HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(tmpModel), headers);
        String resultStr = restTemplate.postForObject("/invoke/func", requestEntity, String.class);
        System.out.println(resultStr);
    }

    @Test
    void testDeleteMultiData(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(type);
        InvokeMethodParamMoreDataDTO tmpModel = new InvokeMethodParamMoreDataDTO();
        tmpModel.setFuncCode("010209_delete2");
        List<HashMap<String, String>> dataset = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("ID", "0000092QY1J1S0000A0U");
        dataset.add(map);
        map = new HashMap<>();
        map.put("ID", "0000092R0UO940000A0W");
        dataset.add(map);
        tmpModel.setDataset(dataset);
        System.out.println(JSON.toJSONString(tmpModel));
        HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(tmpModel), headers);
        String resultStr = restTemplate.postForObject("/invoke/funcmore", requestEntity, String.class);
        System.out.println(resultStr);
    }


    @Data
    class tmpModel {
        private String funcCode;
        private HashMap<String, String> data;
    }
}
