package com.lubase.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class JavaCommonTest {

    @SneakyThrows
    @Test
    void commonTest() {
        String a = "", b = "", c;
        assert a == b;
        a = "abc";
        assert a == "abc";
        assert a.equals("abc");

    }

    @Test
    void isEmperty() {
        String a = null;
        assert StringUtils.isEmpty(a);
        a = "";
        assert StringUtils.isEmpty(a);
    }

    @Test
    void jsonBoolenToInt() throws JsonProcessingException {
        testC testC = new testC();
        testC.setBl_a(true);
        testC.setBl_b(false);
        testC.setL_a(1001111111111111111L);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(testC);
        System.out.println(s);
    }

    @Test
    void splitTest(){
        var str="1,2,3,4,,,3,2,,3,4,3,";
        var str2="";
        String[] split = str2.split(",");
        str2=null;
         split = str2.split(",");
        split = str.split(",");

        String[] split2 = str.split(",",-1);
        List<String> collect = Arrays.stream(split2).filter(m -> !m.isEmpty()).collect(Collectors.toList());
        String[] newSplit2 = collect.toArray(new String[collect.size()]);
        String[] split1 = StringUtils.split(str, ",");
        assert  split.length==13;
    }
}
  @Data
  class  testC{
    Boolean bl_a;
      Boolean bl_b;
    Long l_a;
}
