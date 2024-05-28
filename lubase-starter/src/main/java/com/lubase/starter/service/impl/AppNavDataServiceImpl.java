package com.lubase.starter.service.impl;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.model.DbCollection;
import com.lubase.core.operate.EOperateMode;
import com.lubase.core.service.DataAccess;
import com.lubase.core.util.TableFilterWrapper;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.starter.constant.CacheRightConstant;
import com.lubase.starter.constant.CommonConstant;
import com.lubase.starter.auto.entity.SsPageEntity;
import com.lubase.starter.model.NavVO;
import com.lubase.starter.service.AppNavDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppNavDataServiceImpl implements AppNavDataService {
    @Autowired
    DataAccess dataAccess;

    @Override
    public List<DbEntity> getAllAppInfo() {
        QueryOption queryOption = new QueryOption("ss_app");
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection.getData();
    }

    /**
     * 获取应用的配置管理员和应用管理员
     *
     * @param appId
     * @return
     */
    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_APP_MANAGER + "+#appId")
    @Override
    public String getAppManager(Long appId) {
        DbCollection collection = dataAccess.queryById("ss_app", appId, "app_owner,config_admin");
        if (collection.getData().size() == 0) {
            return "";
        } else {
            return TypeConverterUtils.object2String(collection.getData().get(0).get("app_owner"))
                    + TypeConverterUtils.object2String(collection.getData().get(0).get("config_admin"));
        }
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_ADMIN)
    @Override
    public List<NavVO> getAdminNavData() {
        TableFilter filter = new TableFilter("page_code", "01", EOperateMode.LikeLeft);
        List<NavVO> allNavVOList = getNav(CommonConstant.SYSTEM_APP_ID, filter);
        return allNavVOList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_SETTING)
    @Override
    public List<NavVO> getSettingNavData() {
        TableFilter filter = new TableFilter("page_code", "02", EOperateMode.LikeLeft);
        List<NavVO> allNavVOList = getNav(CommonConstant.SYSTEM_APP_ID, filter);
        return allNavVOList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_APP + "+#appId")
    @Override
    public List<NavVO> getNavDataByAppId(Long appId) {
        if (appId == null) {
            return new ArrayList<>();
        }
        List<NavVO> allNavVOList = getNav(appId, null);
        return allNavVOList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_APP + "+#pageId")
    @Override
    public List<NavVO> getNavDataByPageId(Long pageId) {
        if (pageId == null) {
            return new ArrayList<>();
        }
        TableFilter filter = new TableFilter("parent_id", pageId, EOperateMode.Equals);
        List<NavVO> allNavVOList = getNav(null, filter);
        return allNavVOList;
    }

    List<NavVO> getNav(Long appId, TableFilter extendFilter) {
        //获取用户信息
        QueryOption queryOption = new QueryOption("ss_page");
        queryOption.setFixField("id,page_code,page_name,description,parent_id,order_id,vue_router,vue_component,icon_code,type");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(SsPageEntity.COL_VISIBLE, 1);
        if (appId != null) {
            filterWrapper.eq(SsPageEntity.COL_APP_ID, appId);
        }
        if (extendFilter != null) {
            filterWrapper.addFilter(extendFilter);
        }
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<SsPageEntity> pageList = collection.getGenericData(SsPageEntity.class);

        List<NavVO> navVOList = new ArrayList<>();
        for (SsPageEntity page : pageList) {
            NavVO navVO = new NavVO();
            navVO.setId(page.getId());
            navVO.setParentId(page.getParent_id());
            navVO.setCode(page.getPage_code());
            navVO.setName(page.getPage_name());
            if (page.getPage_name().startsWith("group-")) {
                navVO.setName(page.getPage_name().substring(6));
                navVO.setPageGroup(1);
            } else {
                navVO.setPageGroup(0);
            }
            // 1：一级页面  2 二级页面  3 功能页面
            // 此处是为了兼容前端处理逻辑，因为前端只处理了1和2 待前端升级完成后再优化此部分  2023/10/05 AW
            if (page.getType().equals(1)) {
                navVO.setType(1);
            } else {
                navVO.setType(2);
            }
            navVO.setDes(page.getDescription());
            navVO.setOrderId(page.getOrder_id());
            //为 null 前端加载不出来
            navVO.setVueComponent(page.getVue_component());
            navVO.setVueRouter(page.getVue_router());
            navVO.setIconCode(page.getIcon_code());
            navVOList.add(navVO);
        }
        return navVOList;
    }
}
