package com.lcp.qibao.service.button;

import com.alibaba.fastjson.JSON;
import com.lcp.core.exception.InvokeCommonException;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.qibao.auto.entity.SsButtonEntity;
import com.lcp.qibao.auto.entity.SsPageEntity;
import com.lcp.qibao.model.ButtonServerSettingModel;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 此服务按说应该抽象到页面服务中
 */
public interface SpecialButtonService {
    /**
     * 获取主表code
     *
     * @param button
     * @return
     * @throws InvokeCommonException
     */
    default ButtonServerSettingModel getMainTableCode(SsButtonEntity button) throws InvokeCommonException {
        String serverSettingStr = TypeConverterUtils.object2String(button.get("server_setting"), "");
        if (!StringUtils.isEmpty(serverSettingStr)) {
            ButtonServerSettingModel serverSettingModel = JSON.parseObject(serverSettingStr, ButtonServerSettingModel.class);
            if (serverSettingModel != null && !StringUtils.isEmpty(serverSettingModel.getMainTableCode())) {
                return serverSettingModel;
            }
        }
        throw new InvokeCommonException("按钮未配置主表信息，请联系管理员进行配置");
    }
}
