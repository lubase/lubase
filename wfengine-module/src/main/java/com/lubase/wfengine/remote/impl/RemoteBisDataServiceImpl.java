package com.lubase.wfengine.remote.impl;


import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.core.extend.WorkFlowExtendForService;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import com.lubase.wfengine.remote.RemoteBisDataService;
import com.lubase.wfengine.remote.SendEventToRemoteService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class RemoteBisDataServiceImpl implements ExtendAppLoadCompleteService, RemoteBisDataService {
    @Autowired
    DataAccess dataAccess;

    List<WorkFlowExtendForService> bisDataForWorkFlowList = null;

    @Autowired
    SendEventToRemoteService remoteMQService;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (bisDataForWorkFlowList == null) {
            bisDataForWorkFlowList = new ArrayList<>();
            Map<String, WorkFlowExtendForService> triggerMap = applicationContext.getBeansOfType(WorkFlowExtendForService.class);
            for (String key : triggerMap.keySet()) {
                bisDataForWorkFlowList.add(triggerMap.get(key));
            }
        }
    }

    @Override
    public void clearData() {
        bisDataForWorkFlowList = null;
    }

    @Override
    public List<WorkFlowExtendForService> bisDataForWorkFlowList() {
        return bisDataForWorkFlowList;
    }

    @SneakyThrows
    @Override
    public DbCollection getBisData(String serviceId, String dataId) {
        WorkFlowExtendForService getBisDataForWorkFlow = getInstance(serviceId);
        DbCollection collBisData = null;
        if (getBisDataForWorkFlow != null) {
            collBisData = getBisDataForWorkFlow.getData(serviceId, dataId);
        } else {
            collBisData = getBisDataDefaultMethod(serviceId, dataId);
        }
        if (collBisData == null || collBisData.getData().size() != 1) {
            throw new WarnCommonException(String.format("流程引擎：获取远端业务数据错误，serviceId is %s,dataId is %s", serviceId, dataId));
        }
        return collBisData;
    }

    WorkFlowExtendForService getInstance(String serviceId) {
        if (bisDataForWorkFlowList != null) {
            log.info("start wf");
            log.info(JSON.toJSONString(bisDataForWorkFlowList));
            log.info(serviceId);
            for (WorkFlowExtendForService obj : bisDataForWorkFlowList) {
                if (obj == null || obj.getServiceId() == null) {
                    continue;
                }
                if (obj.getServiceId().equals(serviceId)) {
                    return obj;
                }
            }
        }
        return null;
    }

    @SneakyThrows
    DbCollection getBisDataDefaultMethod(String serviceId, String dataId) {
        DbCollection collService = dataAccess.queryById(WfServiceEntity.TABLE_CODE, Long.parseLong(serviceId));
        if (collService.getData().size() == 0) {
            throw new WarnCommonException(String.format("流程模板配置不正确, serviceId is %s", serviceId));
        }
        String mainTable = collService.getData().get(0).get(WfServiceEntity.COL_MAIN_TABLE).toString();
        String queryField = TypeConverterUtils.object2String(collService.getData().get(0).get("query_field"));
        if (StringUtils.isEmpty(queryField)) {
            queryField = "*";
        }
        QueryOption queryOption = new QueryOption(mainTable);
        queryOption.setFixField(queryField);
        queryOption.setTableFilter(new TableFilter("id", dataId));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        if (collection.getData().size() == 0) {
            throw new WarnCommonException(String.format("未找到业务数据，请联系管理员, dataId is %s", dataId));
        }
        return collection;
    }
}
