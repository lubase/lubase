package com.lubase.wfengine.invoke;

import com.lubase.orm.service.AppHolderService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class SubmitWorkFlowAgain implements IInvokeMethod {
    @Autowired
    WorkFlowService workFlowService;

    @Autowired
    AppHolderService appHolderService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String oInsId = checkAndGetParam("oInsId", hashMap);
        return workFlowService.restartWf(oInsId, appHolderService.getUser().getId().toString());
    }

    @Override
    public String getDescription() {
        return "工作流：退回到开始后再次提交流程";
    }

    @Override
    public String getId() {
        return "814519156803112960";
    }
}
