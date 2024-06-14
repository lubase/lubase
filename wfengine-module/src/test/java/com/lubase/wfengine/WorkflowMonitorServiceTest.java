package com.lubase.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.wfengine.model.ReturnInfoModel;
import com.lubase.wfengine.model.TransferInfoModel;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WorkflowMonitorServiceTest {
    @Autowired
    WorkflowMonitorService monitorService;

    @Test
    void testGetReturnList() {
        String fInsId = "943517530251595776";
        List<ReturnInfoModel> list = monitorService.getReturnBackTaskList(fInsId);

        System.out.println(list);
    }

    @Test
    void testGetUserList() {
        String fInsId = "943213765065707520";
        List<TransferInfoModel> list = monitorService.getCurrentApprovalUser(fInsId);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    void testReturn() {
        String fInsId = "943517530251595776";
        String taskId = "769461010686808064";
        String memo = "test";
        monitorService.returnBack(fInsId, taskId, memo,"");
    }
}
