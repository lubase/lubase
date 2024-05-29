package com.lubase.wfengine.service.button;

import com.lubase.orm.QueryOption;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.extend.PageJumpDataService;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.model.PageDataWFExtendModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class ButtonGetFInsByDataIdImpl implements PageJumpDataService {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    AppHolderService appHolderService;
    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @Override
    public String getId() {
        return "921125596862353408";
    }

    @Autowired
    PageJumpUtil pageJumpUtil;

    @Override
    public String getDescription() {
        return "PageJumpDataService：根据业务数据id获取流程实例id";
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
        return getFInsEntity(serviceId, dataId);
    }

    @SneakyThrows
    DbEntity getFInsEntity(String serviceId, String dataId) {
        QueryOption queryOption = new QueryOption(WfFInsEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("service_id", serviceId).eq("data_id", dataId);
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setFixField("id");
        List<DbEntity> list = dataAccess.queryAllData(queryOption).getData();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new WarnCommonException(String.format("未找到流程实例数据，请联系管理员进行检查:serviceId is %s,dataId is %s", list.size(), serviceId, dataId));
        }
    }
}