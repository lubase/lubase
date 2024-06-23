package com.lubase.wfengine.invoke;

import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.service.AppHolderService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.wfengine.service.WFApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 工作流：获取待办处理页面
 */
@Component
public class GetApprovalFormInvoke implements IInvokeMethod {
    @Autowired
    AppHolderService appHolderService;

    @Autowired
    WFApprovalService approvalService;


    @Override
    public Object exe(HashMap<String, String> hashMap) throws Exception {
        String oInsId = "";
        if (hashMap.containsKey("oInsId")) {
            oInsId = hashMap.get("oInsId");
        }
        if (StringUtils.isEmpty(oInsId)) {
            return new ParameterNotFoundException("oInsId");
        }
        String userId = appHolderService.getUser().getId().toString();
        return approvalService.getApprovalForm(oInsId, userId);
    }

    @Override
    public String getDescription() {
        return "工作流：获取待办处理页面";
    }

    @Override
    public String getId() {
        return "773567309020139520";
    }
}
