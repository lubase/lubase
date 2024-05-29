package com.lubase.wfengine.service.button;

import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.extend.PageJumpDataService;
import com.lubase.wfengine.model.CustomPageSetting;
import com.lubase.wfengine.model.PageDataWFExtendModel;
import com.lubase.wfengine.service.WFApprovalService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class ButtonGetOInsByDataIdImpl implements PageJumpDataService {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    AppHolderService appHolderService;
    @Autowired
    ChangeDataSourceService changeDataSourceService;
    @Autowired
    WFApprovalService approvalService;

    @Autowired
    PageJumpUtil pageJumpUtil;

    @Override
    public String getId() {
        return "921125158993793024";
    }

    @Override
    public String getDescription() {
        return "PageJumpDataService：根据业务数据id获取处理人实例id";
    }

    @Override
    public DbEntity convertClientData(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        if (mapParam == null) {
            throw new InvokeCommonException("参数不正确，请检查");
        }
        String dataId = checkAndGetParam("id", mapParam);
        DbCollection collPage = dataAccess.queryById(SsPageEntity.TABLE_CODE, button.getPage_id());
        if (collPage.getData().size() != 1) {
            throw new WarnCommonException("按钮所属页面数据获取失败，请检查");
        }
        PageDataWFExtendModel dataWFExtendModel = pageJumpUtil.getServiceId(button, collPage.getData().get(0));
        if (dataWFExtendModel == null || StringUtils.isEmpty(dataWFExtendModel.getServiceId())) {
            throw new WarnCommonException("页面流程配置不正确");
        }
        String serviceId = dataWFExtendModel.getServiceId();
        String userId = appHolderService.getUser().getId().toString();
        DbEntity oInsEntity = getOInsEntity(serviceId, dataId, userId);

        CustomPageSetting refPageSetting = approvalService.getRefPageSetting(oInsEntity.getId().toString(), userId);
        if (refPageSetting != null && !StringUtils.isEmpty(refPageSetting.getNavCode())) {
            oInsEntity.put(NAVCODE, refPageSetting.getNavCode());
        }
        oInsEntity.put(ORIGINALID, dataId);
        return oInsEntity;
    }

    @SneakyThrows
    DbEntity getOInsEntity(String serviceId, String dataId, String userId) {
        changeDataSourceService.changeDataSourceByTableCode("wf_app");
        List<DbEntity> list = dataAccess.procGetDbEntityList("proc_getWfOInsByDataId", userId, serviceId, dataId);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new WarnCommonException(String.format("找到%s条待处理实例数据，请联系管理员进行检查:serviceId is %s,dataId is %s", list.size(), serviceId, dataId));
        }
    }
}
