package com.lcp.qibao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MapperTest {

   // @Autowired
   // SuuserMapper mapper;

   // @Autowired
  //  ISsfuncService ssfuncService;


    @Test
    void getDataByXmlMapper() {
//        String code = mapper2.selectByMapper("000005KTNSIHL0000A00");
//        System.out.println("code is " + code);
    }

    @Test
    void GetUserById() {
       // Suuser user = mapper.selectById("0000000H");
      //  System.out.println(user);

    }

    @Autowired
    //SspageMapper sspageMapper;

    @Test
    void GetBtnByCode() {
        String pcode = "010109";
       // List<ButtonVO> list = sspageMapper.getPageButton(pcode);
       // System.out.println(list);

        //System.out.println(list.size());
    }

    @Test
    void GetParms(@Autowired TestRestTemplate restTemplate) {
        String pcode = "010109";
        Map<String, String> map = new HashMap<>();
        map.put("pcode", pcode);
        String str2 = restTemplate.getForObject("/ssfunc/getButtons?pcode={pcode}", String.class, map);
        System.out.println("str2:" + str2);
    }



}
