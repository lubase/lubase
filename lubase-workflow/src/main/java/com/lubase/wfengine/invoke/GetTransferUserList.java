package com.lubase.wfengine.invoke;

import com.lubase.starter.extend.IInvokeMethod;
import com.lubase.wfengine.model.TransferInfoModel;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class GetTransferUserList implements IInvokeMethod {

    @Autowired
    WorkflowMonitorService monitorService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String fInsId = checkAndGetParam("fInsId", hashMap);
        List<TransferInfoModel> list = monitorService.getCurrentApprovalUser(fInsId);
        return list;
    }

    @Override
    public String getDescription() {
        return "流程监控：获取待转办人员列表";
    }

    @Override
    public String getId() {
        return "943214791009243136";
    }
}
