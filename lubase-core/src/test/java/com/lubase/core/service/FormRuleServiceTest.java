package com.lubase.core.service;

import com.alibaba.fastjson.JSON;
import com.lubase.core.model.customForm.FormRule;
import com.lubase.core.service.FormRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FormRuleServiceTest {

    @Autowired
    FormRuleService formRuleService;

    @Test
    void testControlRule() {
        Long formId = 2022052900033761796L;
        FormRule rule = formRuleService.getFormRuleById(formId);

        assert rule != null;
        System.out.println(rule.getFieldRely().size());
        assert rule.getFieldControl().size() == 1;
        String str = JSON.toJSONString(rule);
        System.out.println(str);
    }
}
