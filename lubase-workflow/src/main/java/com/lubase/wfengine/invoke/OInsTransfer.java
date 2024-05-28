package com.lubase.wfengine.invoke;

import com.lubase.core.service.AppHolderService;
import com.lubase.starter.extend.IInvokeMethod;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class OInsTransfer implements IInvokeMethod {
    @Autowired
    WorkflowMonitorService monitorService;

    @Autowired
    AppHolderService holderService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String id = checkAndGetParam("oInsId", hashMap);
        String toUserId = checkAndGetParam("toUserId", hashMap);
        String currentUserId = holderService.getUser().getId().toString();
        String memo = "";
        if (hashMap.containsKey("memo")) {
            memo = hashMap.get("memo").substring(100);
        }
        return monitorService.transfer(id, currentUserId, toUserId);
    }

    @Override
    public String getDescription() {
        return "流程监控：管理员转办任务";
    }

    @Override
    public String getId() {
        return "928610088191528970";
    }
}
