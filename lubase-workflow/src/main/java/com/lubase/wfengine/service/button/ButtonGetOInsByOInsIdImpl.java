package com.lubase.wfengine.service.button;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.service.AppHolderService;
import com.lubase.model.DbEntity;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.extend.PageJumpDataService;
import com.lubase.wfengine.model.CustomPageSetting;
import com.lubase.wfengine.service.WFApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Slf4j
@Component
public class ButtonGetOInsByOInsIdImpl implements PageJumpDataService {
    @Autowired
    AppHolderService appHolderService;

    @Autowired
    WFApprovalService approvalService;

    @Override
    public String getId() {
        return "922557615194509312";
    }

    @Override
    public String getDescription() {
        return "PageJumpDataService：根据流程实例id获取跳转待办信息";
    }

    @Override
    public DbEntity convertClientData(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        if (mapParam == null) {
            throw new InvokeCommonException("参数不正确，请检查");
        }
        String oInsId = checkAndGetParam("id", mapParam);
        String userId = appHolderService.getUser().getId().toString();
        CustomPageSetting refPageSetting = approvalService.getRefPageSetting(oInsId, userId);
        DbEntity oInsEntity = new DbEntity();
        oInsEntity.cloneFromNewEntity(mapParam);
        if (refPageSetting != null && !StringUtils.isEmpty(refPageSetting.getNavCode())) {
            oInsEntity.put(NAVCODE, refPageSetting.getNavCode());
        }
        oInsEntity.put(ORIGINALID,oInsId);
        return oInsEntity;
    }
}
