package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.lubase.starter.auto.entity.DmTableRelationEntity;
import com.lubase.starter.service.FormRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FormRuleServiceTest {

    @Autowired
    FormRuleService formRuleService;

    @Test
    void test() {
        String formId = "817493282010435584";
        String serialNum = "uJ9Xr7tIK1h02afDjvDsUfXM5V5dIX1M";

        String childTableId = formRuleService.getChildTableId(formId, serialNum);
        String mainTableId = formRuleService.getMainTableId(formId);
        System.out.println(String.format("mainTableId is %s,childTableId is %s", mainTableId, childTableId));
        assert childTableId.equals("769373802558656512");
        assert mainTableId.equals("769370645841580032");

        DmTableRelationEntity tableRelationEntity = formRuleService.getTableRelation(mainTableId, childTableId);
        assert tableRelationEntity != null;
        System.out.println(JSON.toJSONString(tableRelationEntity));
        System.out.println(tableRelationEntity.getFk_column_code());
        assert tableRelationEntity.getFk_column_code().equals("task_id");
    }
}
