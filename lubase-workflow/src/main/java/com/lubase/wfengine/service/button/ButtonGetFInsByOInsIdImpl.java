package com.lubase.wfengine.service.button;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.AppHolderService;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.extend.PageJumpDataService;
import com.lubase.wfengine.model.CustomPageSetting;
import com.lubase.wfengine.service.WFApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Component
public class ButtonGetFInsByOInsIdImpl implements PageJumpDataService {

    @Autowired
    DataAccess dataAccess;
    @Autowired
    WFApprovalService approvalService;
    @Autowired
    AppHolderService appHolderService;

    @Override
    public String getId() {
        return "921126188259217408";
    }

    @Override
    public String getDescription() {
        return "PageJumpDataService：根据处理者实例id获取流程实例id";
    }

    @Override
    public DbEntity convertClientData(SsButtonEntity ssButtonEntity, HashMap<String, String> hashMap) throws Exception {
        String oInsId = checkAndGetParam("id", hashMap);

        QueryOption queryOption = new QueryOption("wf_oins");
        queryOption.setFixField("fins_id");
        queryOption.setTableFilter(new TableFilter("id", oInsId));
        DbCollection collOIns = dataAccess.queryAllData(queryOption);
        String userId = appHolderService.getUser().getId().toString();
        CustomPageSetting refPageSetting = approvalService.getRefPageSetting(oInsId, userId);

        DbEntity fInsEntity = new DbEntity();
        fInsEntity.putWithNoTrace(ORIGINALID, oInsId);
        fInsEntity.put(ORIGINALID, oInsId);
        if (collOIns.getData().size() == 1) {
            fInsEntity.put("id", collOIns.getData().get(0).get("fins_id").toString());
        } else {
            fInsEntity.put("id", 0);
        }
        if (refPageSetting != null && !StringUtils.isEmpty(refPageSetting.getNavCode())) {
            fInsEntity.put(NAVCODE, refPageSetting.getNavCode());
            fInsEntity.putWithNoTrace(NAVCODE, refPageSetting.getNavCode());
        }
        return fInsEntity;
    }
}
