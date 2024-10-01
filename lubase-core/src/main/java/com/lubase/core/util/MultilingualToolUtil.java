package com.lubase.core.util;

import com.lubase.core.model.NavVO;
import com.lubase.model.ResourceDataModel;
import com.lubase.orm.service.RegisterColumnInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * 多语言处理类
 */
@Service
public class MultilingualToolUtil {

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    public void processCurrentLanguage(List<NavVO> navList, String appId) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            return;
        }
        List<ResourceDataModel> resourceList = registerColumnInfoService.getResourceList(appId);
        if (resourceList == null || resourceList.isEmpty()) {
            return;
        }
        navList.parallelStream().forEach(navVO -> {
            ResourceDataModel resourceDataModel = resourceList.stream().filter(x -> x.getDataId().equals(navVO.getId().toString())
                    && x.getField().equals("page_name")).findFirst().orElse(null);
            if (resourceDataModel != null) {
                navVO.setName(resourceDataModel.getMsg());
            }
        });
    }
}
