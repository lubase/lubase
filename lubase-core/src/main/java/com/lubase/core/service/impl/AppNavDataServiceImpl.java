package com.lubase.core.service.impl;

import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.constant.CommonConstant;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.model.NavVO;
import com.lubase.core.service.AppNavDataService;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
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
        List<NavVO> allNavVOList = getNav(CommonConstant.SYSTEM_APP_ID, filter, true);
        return allNavVOList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_SETTING)
    @Override
    public List<NavVO> getSettingNavData() {
        TableFilter filter = new TableFilter("page_code", "02", EOperateMode.LikeLeft);
        List<NavVO> allNavVOList = getNav(CommonConstant.SYSTEM_APP_ID, filter, true);
        return allNavVOList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_APP + "+#appId")
    @Override
    public List<NavVO> getNavDataByAppId(Long appId) {
        if (appId == null) {
            return new ArrayList<>();
        }
        List<NavVO> allNavVOList = getNav(appId, null, true);
        return allNavVOList;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PAGE_APP + "+#pageId")
    @Override
    public List<NavVO> getNavDataByPageId(Long pageId) {
        if (pageId == null) {
            return new ArrayList<>();
        }
        TableFilter filter = new TableFilter("parent_id", pageId, EOperateMode.Equals);
        List<NavVO> allNavVOList = getNav(null, filter, false);
        return allNavVOList;
    }

    @Override
    public NavVO getNavInfoByPageId(Long pageId) {
        if (pageId == null) {
            return null;
        }
        TableFilter filter = new TableFilter("id", pageId, EOperateMode.Equals);
        List<NavVO> allNavVOList = getNav(null, filter, false);
        if (allNavVOList.isEmpty()) {
            return null;
        } else {
            return allNavVOList.get(0);
        }
    }

    List<NavVO> getNav(Long appId, TableFilter extendFilter, boolean filterLevel2) {
        //获取用户信息
        QueryOption queryOption = new QueryOption("ss_page");
        queryOption.setFixField("id,page_name,description,parent_id,order_id,vue_router,vue_component,icon_code,type,master_page");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(SsPageEntity.COL_VISIBLE, 1);
        // 1： 菜单，4：菜单分组。不显示2：二级页面 3 公共页面
        if (filterLevel2) {
            filterWrapper.in(SsPageEntity.COL_TYPE, "1,4");
        }
        if (appId != null) {
            filterWrapper.eq(SsPageEntity.COL_APP_ID, appId);
        }
        if (extendFilter != null) {
            filterWrapper.addFilter(extendFilter);
        }
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<SsPageEntity> pageList = collection.getGenericData(SsPageEntity.class);
        List<Long> tabPageIdList = new ArrayList<>();
        if (filterLevel2) {
            for (SsPageEntity page : pageList) {
                if (CommonConstant.TAB_PAGE_MASTER_PAGE_CODE.equals(page.getPage_code())) {
                    tabPageIdList.add(page.getId());
                }
            }
        }
        List<NavVO> navVOList = new ArrayList<>();
        for (SsPageEntity page : pageList) {
            // 过滤页签页面的子页面
            if (filterLevel2 && tabPageIdList.contains(page.getParent_id())) {
                continue;
            }
            NavVO navVO = new NavVO();
            navVO.setId(page.getId());
            navVO.setParentId(page.getParent_id());
            navVO.setName(page.getPage_name());
            navVO.setType(page.getType());
            navVO.setDes(page.getDescription());
            navVO.setOrderId(page.getOrder_id());
            navVO.setVueComponent(page.getVue_component());
            navVO.setVueRouter(page.getVue_router());
            navVO.setIconCode(page.getIcon_code());
            navVOList.add(navVO);
        }
        return navVOList;
    }
}
