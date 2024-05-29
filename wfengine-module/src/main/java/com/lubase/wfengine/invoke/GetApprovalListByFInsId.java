package com.lubase.wfengine.invoke;

import com.lubase.orm.model.DbCollection;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WFApprovalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class GetApprovalListByFInsId implements IInvokeMethod {

    @Autowired
    WFApprovalService approvalService;

    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String fInsId = checkAndGetParam("fInsId", hashMap);
        DbCollection coll = approvalService.getApprovalList(fInsId);
        return coll;
    }

    @Override
    public String getDescription() {
        return "流程引擎：根据流程实例id获取流程审批记录";
    }

    @Override
    public String getId() {
        return "947234442487795712";
    }
}
