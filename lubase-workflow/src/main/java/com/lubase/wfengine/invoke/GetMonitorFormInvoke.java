package com.lubase.wfengine.invoke;

import com.lubase.core.service.AppHolderService;
import com.lubase.starter.extend.IInvokeMethod;
import com.lubase.wfengine.service.WFApprovalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 工作流：获取流程监控处理页面
 */
public class GetMonitorFormInvoke implements IInvokeMethod {
    @Autowired
    AppHolderService appHolderService;

    @Autowired
    WFApprovalService approvalService;


    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String fInsId = checkAndGetParam("fInsId", hashMap);
        String oInsId = "";
        if (hashMap.containsKey("oInsId")) {
            oInsId = hashMap.get("oInsId");
        }
        return approvalService.getApprovalHistoryForm(fInsId, oInsId);
    }

    @Override
    public String getDescription() {
        return "工作流：获取流程监控详情页面";
    }

    @Override
    public String getId() {
        return "916518482282024960";
    }
}
