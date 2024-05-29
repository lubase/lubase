package com.lubase.core.service;

import com.lubase.model.DbEntity;
import com.lubase.core.model.NavVO;

import java.util.List;

public interface AppNavDataService {
    /**
     * 获取系统应用列表
     *
     * @return
     */
    List<DbEntity> getAllAppInfo();

    /**
     * 获取应用管理员列表,配置管理员和应用开发管理员
     *
     * @param appId
     * @return
     */
    String getAppManager(Long appId);

    /**
     * 获取后端配置菜单列表
     *
     * @return
     */
    List<NavVO> getAdminNavData();

    /**
     * 获取后端配置菜单列表
     *
     * @return
     */
    List<NavVO> getSettingNavData();

    /**
     * 获取应用下的菜单列表
     *
     * @return
     */
    List<NavVO> getNavDataByAppId(Long appId);

    /**
     * 获取某个页面下的子页面列表
     *
     * @param pageId
     * @return
     */
    List<NavVO> getNavDataByPageId(Long pageId);
}
