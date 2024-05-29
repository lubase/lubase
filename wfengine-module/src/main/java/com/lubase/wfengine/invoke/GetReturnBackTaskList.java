package com.lubase.wfengine.invoke;

import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class GetReturnBackTaskList implements IInvokeMethod {

    @Autowired
    WorkflowMonitorService monitorService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String fInsId=checkAndGetParam("fInsId",hashMap);
        return monitorService.getReturnBackTaskList(fInsId);
    }

    @Override
    public String getDescription() {
        return "流程监控：获取可退回的流程节点";
    }

    @Override
    public String getId() {
        return "943448033159286784";
    }
}
