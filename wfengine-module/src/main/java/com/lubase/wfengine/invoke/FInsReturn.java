package com.lubase.wfengine.invoke;

import com.lubase.orm.service.AppHolderService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class FInsReturn implements IInvokeMethod {
    @Autowired
    WorkflowMonitorService monitorService;

    @Autowired
    AppHolderService holderService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String fInsId = checkAndGetParam("fInsId", hashMap);
        String taskId = checkAndGetParam("taskId", hashMap);
        String memo = "";
        if (hashMap.containsKey("memo")) {
            memo = hashMap.get("memo").substring(100);
        }
        return monitorService.returnBack(fInsId, taskId, memo, holderService.getUser().getId().toString());
    }

    @Override
    public String getDescription() {
        return "流程监控：流程退回";
    }

    @Override
    public String getId() {
        return "943833597322727424";
    }
}
