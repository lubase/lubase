package com.lubase.wfengine.invoke;

import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.service.AppHolderService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WorkflowMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class FInsGiveUp implements IInvokeMethod {
    @Autowired
    WorkflowMonitorService monitorService;

    @Autowired
    AppHolderService appHolderService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String finsId = checkAndGetParam("id", hashMap);
        String memo = "";
        if (hashMap.containsKey("memo")) {
            memo = hashMap.get("memo").substring(100);
        }
        Integer result = monitorService.giveUp(appHolderService.getUser(), finsId, memo);
        if (result.equals(1)) {
            return true;
        }
        throw new WarnCommonException("流程废弃失败");
    }

    @Override
    public String getDescription() {
        return "流程监控：流程实例废弃";
    }

    @Override
    public String getId() {
        return "928610088191528960";
    }
}
