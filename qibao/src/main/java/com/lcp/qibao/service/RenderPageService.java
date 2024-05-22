package com.lcp.qibao.service;

import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.model.NavVO;
import com.lcp.qibao.model.PageInfoVO;
import com.lcp.qibao.util.ClientMacro;

import java.util.List;

public interface RenderPageService {
    /**
     * 获取用户所有可见应用下的菜单信息
     *
     * @return
     */
    List<NavVO> getAppAllNavData();

    /**
     * 获取某个应用下菜单
     *
     * @return
     */
    List<NavVO> getAppNavDataByApId(Long appId);

    /**
     * 获取某个页面下的子页面
     *
     * @return
     */
    List<NavVO> getNavDataByPageId(Long appId, ClientMacro clientMacro);

    /**
     * 获取扩展显示类型列表
     *
     * @return
     */
    List<DbEntity> getExtendDisplayType();

    /**
     * 根据页面代码获取页面的渲染配置信息
     *
     * @param pageId
     * @return
     */
    PageInfoVO getPageInfo(String pageId);
}
