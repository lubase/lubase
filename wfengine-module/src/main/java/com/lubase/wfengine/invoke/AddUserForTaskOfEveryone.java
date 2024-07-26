package com.lubase.wfengine.invoke;

import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 会签节点增加处理人
 */
public class AddUserForTaskOfEveryone implements IInvokeMethod {

    @Autowired
    WorkflowMonitorService workflowMonitorService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String fInsId = checkAndGetParam("fInsId", hashMap);
        String userIds = checkAndGetParam("userIds", hashMap);

        return workflowMonitorService.addUserForTaskOfEveryone(fInsId, userIds.split(","));
    }

    @Override
    public String getDescription() {
        return "流程引擎：为会签节点增加处理人";
    }

    @Override
    public String getId() {
        return "1124828272446672896";
    }
}
