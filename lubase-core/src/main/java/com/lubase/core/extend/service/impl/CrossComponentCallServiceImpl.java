package com.lubase.core.extend.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.core.extend.CrossComponentService;
import com.lubase.core.extend.service.CrossComponentCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrossComponentCallServiceImpl implements CrossComponentCallService, ExtendAppLoadCompleteService {

    List<CrossComponentService> componentServiceList;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (componentServiceList == null) {
            componentServiceList = new ArrayList<>(applicationContext.getBeansOfType(CrossComponentService.class).values());
        }
        log.info(String.format("CrossComponentService列表总数量为：%s，明细为：%s", componentServiceList.size(), JSON.toJSONString(componentServiceList)));
    }

    @Override
    public void clearData() {
        componentServiceList = null;
    }

    @Override
    public CrossComponentService getServiceByIdentification(String id) {
        if (componentServiceList == null || StringUtils.isEmpty(id)) {
            return null;
        }
        for (CrossComponentService service : componentServiceList) {
            if (service.getServiceIdentification().equals(id)) {
                return service;
            }
        }
        return null;
    }

}
