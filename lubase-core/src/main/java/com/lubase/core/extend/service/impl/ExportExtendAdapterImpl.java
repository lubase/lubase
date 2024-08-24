package com.lubase.core.extend.service.impl;

import com.lubase.core.extend.ExportExtendService;
import com.lubase.core.extend.service.ExportExtendAdapter;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExportExtendAdapterImpl implements ExportExtendAdapter, ExtendAppLoadCompleteService {

    List<ExportExtendService> exportExtendServices;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (exportExtendServices == null) {
            exportExtendServices = new ArrayList<>(applicationContext.getBeansOfType(ExportExtendService.class).values());
        }
    }

    @Override
    public void clearData() {
        exportExtendServices = null;
    }

    @Override
    public ExportExtendService getServiceByIdentification(String identityCode) {
        if (exportExtendServices == null || StringUtils.isEmpty(identityCode)) {
            return null;
        }
        for (ExportExtendService service : exportExtendServices) {
            if (service.getTableIdentityCode().contains(identityCode)) {
                return service;
            }
        }
        return null;
    }

}
