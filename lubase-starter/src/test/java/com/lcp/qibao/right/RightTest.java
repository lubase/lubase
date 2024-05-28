package com.lcp.qibao.right;

import com.alibaba.fastjson.JSONObject;
import com.lubase.starter.controller.InvokeController;
import com.lubase.starter.model.InvokeMethodParamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class RightTest {

    @Autowired
    InvokeController invokeController;

    @Test
    public void delActorTest() {
        InvokeMethodParamDTO dto = new InvokeMethodParamDTO();
        HashMap<String, String> map = new HashMap<>();
        map.put("ID","000018A7XKUNB0000A05");
        dto.setData(map);
        dto.setFuncCode("011003_btnDel");
        invokeController.invokeOneData(dto);

    }
    @Test
    public void delActorUserTest() {
        InvokeMethodParamDTO dto = new InvokeMethodParamDTO();
        HashMap<String, String> map = new HashMap<>();
        map.put("ID","000018A7XKUNB0000A06");
        map.put("USER","10000000,20000000");
        dto.setData(map);
        dto.setFuncCode("011003_btnDelAssign");
        invokeController.invokeOneData(dto);

    }

    @Test
    public void getFuncRightTest() {
        InvokeMethodParamDTO dto = new InvokeMethodParamDTO();
        HashMap<String, String> map = new HashMap<>();
        map.put("ID","000018A7XKUNB0000A06");
        dto.setData(map);
        dto.setFuncCode("011004");
        //dto.setMethodId("00001GB3QKYVW0000A08");
        dto.setMethodId(1L);
        Object o = invokeController.method(dto);
        System.out.println(JSONObject.toJSON(o));

    }

    @Test
    public void addAssignTest() {
        InvokeMethodParamDTO dto = new InvokeMethodParamDTO();
        HashMap<String, String> map = new HashMap<>();
        map.put("ID","000018A7XKUNB0000A06");
        map.put("funcid","000005G0R52SP0000163,000005GP8K33A0000000");
        dto.setData(map);
        dto.setFuncCode("011004");
        //dto.setMethodId("00001GB3QKYVW0000A09");
        dto.setMethodId(1L);
        Object o = invokeController.method(dto);
        System.out.println(JSONObject.toJSON(o));

    }
}
