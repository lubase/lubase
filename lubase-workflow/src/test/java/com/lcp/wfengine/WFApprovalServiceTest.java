package com.lcp.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.wfengine.model.WFApprovalFormVO;
import com.lubase.wfengine.service.WFApprovalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WFApprovalServiceTest {
    @Autowired
    WFApprovalService approvalService;

    @Test
    void testGetOIns() {
        String oInsId = "982678759209439232";
        String userId = "688164070687248384";
        WFApprovalFormVO formVO = approvalService.getApprovalForm(oInsId, userId);
        
        System.out.println(JSON.toJSONString(formVO));
    }
}
