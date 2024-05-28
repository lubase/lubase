package com.lubase.starter.service.button.impl;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.extend.service.PageJumpDataServiceAdapter;
import com.lubase.starter.service.button.OndDataService;
import com.lubase.starter.extend.PageJumpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Service
public class Button43ConvertClientDataImpl implements OndDataService {

    @Autowired
    PageJumpDataServiceAdapter pageJumpDataServiceAdapter;

    @Override
    public String getButtonType() {
        return "43";
    }

    @Override
    public String getDescription() {
        return "从表格取值服务端转换然后跳转页面";
    }

    @Override
    public Object exe(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        if (mapParam == null) {
            throw new InvokeCommonException("客户端参数不正确，请检查");
        }
        String refMethodId = TypeConverterUtils.object2String(button.get("method_id"));
        if (StringUtils.isEmpty(refMethodId)) {
            throw new WarnCommonException("按钮未配置关联方法，请配置");
        }
        PageJumpDataService service = pageJumpDataServiceAdapter.getPageJumpDataService(refMethodId);
        if (service == null) {
            throw new WarnCommonException("按钮配置方法未实现请检查");
        }
        return service.convertClientData(button, mapParam);
    }


}
