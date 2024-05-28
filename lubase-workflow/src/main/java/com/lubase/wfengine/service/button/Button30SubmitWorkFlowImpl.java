package com.lubase.wfengine.service.button;

import com.alibaba.fastjson.JSON;
import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.exception.ParameterNotFoundException;
import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.service.AppHolderService;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.service.button.OndDataService;
import com.lubase.wfengine.model.WorkFlowSettingModel;
import com.lubase.wfengine.service.StartFlow;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;


@Service
public class Button30SubmitWorkFlowImpl implements OndDataService {
    @Autowired
    StartFlow flowService;

    @Override
    public String getButtonType() {
        return "30";
    }

    @Autowired
    AppHolderService holderService;

    @Override
    public String getDescription() {
        return "单条业务数据提交流程";
    }

    @Override
    public Object exe(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        if (mapParam == null) {
            throw new InvokeCommonException("参数不正确，请检查");
        }
        Long id = 0L;
        if (mapParam.containsKey(idHandle)) {
            id = Long.valueOf(mapParam.get(idHandle));
        }
        if (StringUtils.isEmpty(id)) {
            throw new ParameterNotFoundException(idHandle);
        }
        String serviceId = getServiceIdByButtonId(button);
        return flowService.startWfByApi(serviceId, id.toString(), holderService.getUser().getId().toString());
    }

    @SneakyThrows
    String getServiceIdByButtonId(SsButtonEntity button) {
        String serverSettingStr = "";
        if (button.containsKey("server_setting")) {
            serverSettingStr = TypeConverterUtils.object2String(button.get("server_setting"));
        }
        WorkFlowSettingModel settingModel = null;
        try {
            settingModel = JSON.parseObject(serverSettingStr, WorkFlowSettingModel.class);
        } catch (Exception exception) {
            throw new WarnCommonException("按钮未配置流程信息，请联系管理员");
        }
        if (settingModel == null || StringUtils.isEmpty(settingModel.getServiceId())) {
            throw new WarnCommonException("按钮未配置流程信息，请联系管理员");
        }
        return settingModel.getServiceId();
    }
}
