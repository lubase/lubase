package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.lubase.core.model.SaveFormParamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DBEntityTest {

    @Test
    void testDeserialize() {
        String str = "{\"funcCode\":\"010109_btnAdd\",\"data\":{\"TYPEID\":\"000018BIZHZ8N0000A0E\",\"CODE\":\"006\",\"NAME\":\"Ad002\"}}";
        Object o = (SaveFormParamDTO)JSON.parse(str);

//        String serializeStr = JSON.toJSONString(entity);
        System.out.println(String.format("字符串为：\r%s", str));
//        System.out.println(String.format("序列化后为：\r%s", serializeStr));

    }
}
