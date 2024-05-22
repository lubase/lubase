package com.lcp.qibao.service.button.impl;

import com.lcp.core.exception.InvokeCommonException;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.qibao.auto.entity.SsButtonEntity;
import com.lcp.qibao.extend.service.PageJumpDataServiceAdapter;
import com.lcp.qibao.service.button.OndDataService;
import com.lcp.qibao.extend.PageJumpDataService;
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
