package com.lubase.core.util;

import com.lubase.core.model.ButtonVO;
import com.lubase.core.model.NavVO;
import com.lubase.model.DbField;
import com.lubase.model.ResourceDataModel;
import com.lubase.orm.service.RegisterColumnInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 多语言处理类
 */
@Service
public class MultilingualToolUtil {

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    /**
     * 导航菜单过后
     *
     * @param navList
     * @param appId
     */
    public void processCurrentLanguageForNav(List<NavVO> navList, String appId) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            return;
        }
        List<ResourceDataModel> resourceList = registerColumnInfoService.getResourceList(appId, "ss_page");
        if (resourceList == null || resourceList.isEmpty()) {
            return;
        }
        String userLanguage = locale.toString();
        List<ResourceDataModel> currentResource = resourceList.stream().filter(x -> x.getUserLanguage().equals(userLanguage)).collect(java.util.stream.Collectors.toList());
        navList.parallelStream().forEach(navVO -> {
            ResourceDataModel resourceDataModel = currentResource.stream().filter(x -> x.getDataId().equals(navVO.getId().toString())
                    && x.getField().equals("page_name")).findFirst().orElse(null);
            if (resourceDataModel != null) {
                navVO.setName(resourceDataModel.getMsg());
            }
            resourceDataModel = currentResource.stream().filter(x -> x.getDataId().equals(navVO.getId().toString())
                    && x.getField().equals("description")).findFirst().orElse(null);
            if (resourceDataModel != null) {
                navVO.setDes(resourceDataModel.getMsg());
            }
        });
    }

    /**
     * 页面按钮国际化
     *
     * @param buttonVOList
     * @param appId
     */
    public void processCurrentLanguageForButton(List<ButtonVO> buttonVOList, String appId) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            return;
        }
        List<ResourceDataModel> resourceList = registerColumnInfoService.getResourceList(appId, "ss_button");
        if (resourceList == null || resourceList.isEmpty()) {
            return;
        }
        String userLanguage = locale.toString();
        List<ResourceDataModel> currentResource = resourceList.stream().filter(x -> x.getUserLanguage().equals(userLanguage)).collect(java.util.stream.Collectors.toList());
        buttonVOList.parallelStream().forEach(btnVO -> {
            ResourceDataModel resourceDataModel = currentResource.stream().filter(x -> x.getDataId().equals(btnVO.getId().toString())
                    && x.getField().equals("button_name")).findFirst().orElse(null);
            if (resourceDataModel != null) {
                btnVO.setName(resourceDataModel.getMsg());
            }
            resourceDataModel = currentResource.stream().filter(x -> x.getDataId().equals(btnVO.getId().toString())
                    && x.getField().equals("group_des")).findFirst().orElse(null);
            if (resourceDataModel != null) {
                btnVO.setGroupDes(resourceDataModel.getMsg());
            }
        });
    }

    /**
     * 搜索区域国际化
     *
     * @param fieldBOHashMap
     * @param appId
     */
    public void processCurrentLanguageForSearch(HashMap<String, DbField> fieldBOHashMap, String appId) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            return;
        }
        List<ResourceDataModel> resourceList = registerColumnInfoService.getResourceList(appId, "dm_column");
        if (resourceList == null || resourceList.isEmpty()) {
            return;
        }
        String userLanguage = locale.toString();
        List<ResourceDataModel> currentResource = resourceList.stream().filter(x -> x.getUserLanguage().equals(userLanguage)).collect(java.util.stream.Collectors.toList());
        fieldBOHashMap.values().parallelStream().forEach(field -> {
            ResourceDataModel resourceDataModel = currentResource.stream().filter(x -> x.getDataId().equals(field.getId())).findFirst().orElse(null);
            if (resourceDataModel != null) {
                field.setName(resourceDataModel.getMsg());
            }
        });
    }
}
