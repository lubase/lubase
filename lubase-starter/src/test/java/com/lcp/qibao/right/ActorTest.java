package com.lcp.qibao.right;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.starter.model.InvokeMethodParamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ActorTest {


    @Test
    public void enumTest() {
        String js = "{\"ttt\":\"4\"}";
        //  js="{\"ttt\":4}";
        // js="{\"EColumnType\":\"4\",\"DisType\":\"42\",\"TableName\":\"SUUSER\",\"ExtendCols\":\"CODE\",\"IsMore\":0,\"TableKey\":\"ID\",\"DisplayCol\":\"NAME\",\"TableFilter\":null}"
        TestE testE = JSON.parseObject(js, TestE.class);
        System.out.println(js);
    }

    @Test
    public void testMap() {
        String strMap = "{\n" +
                "\t\"funcCode\": \"code\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"id\": \"aaaaa\",\n" +
                "\t\t\"user\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"id\": \"bbbb\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"id\": \"ccc\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
        InvokeMethodParamDTO invokeMethodParamDTO = JSON.parseObject(strMap, InvokeMethodParamDTO.class);
        String strJson = JSONObject.toJSONString(invokeMethodParamDTO.getData());
        String id = invokeMethodParamDTO.getData().get("id");
        String user = invokeMethodParamDTO.getData().get("user");
        // strJson=strJson.replace("\\\"","\"");
       // DelActorBO delActorBO = JSON.parseObject(user, DelActorBO.class);
        System.out.println(invokeMethodParamDTO.getData());
    }
}
