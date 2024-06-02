package com.lubase.core.service;

import com.alibaba.fastjson.JSON;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.model.DbField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomFormDataServiceTest {

    @Autowired
    CustomFormDataService customFormDataService;

    @Test
    void testGetForm() {
        String formId = "845296486580228096";
        DmCustomFormEntity customForm = customFormDataService.selectById(formId);
        System.out.println(JSON.toJSONString(customForm));
    }

    @Test
    void testGetFormFieldSetting() {
        String formId = "845296486580228096";
        DmCustomFormEntity customForm = customFormDataService.selectById(formId);
        // [{"id":"2233234234","required":1,"readonly":0}]
        String fieldSetting = "[{\"id\":\"2022052821107755500\",\"required\":1,\"readonly\":1}]";
        customForm.setField_info(fieldSetting);
        List<DbField> fieldList = customFormDataService.getFormFieldSetting(customForm);
        System.out.println(JSON.toJSONString(fieldList));
    }
}
