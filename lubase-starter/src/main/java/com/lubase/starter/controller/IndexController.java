package com.lubase.starter.controller;


import com.lubase.starter.model.NavVO;
import com.lubase.starter.response.ResponseData;
import com.lubase.starter.service.CodeDataService;
import com.lubase.starter.service.RenderPageService;
import com.lubase.starter.service.UserInfoService;
import com.lubase.starter.util.ClientMacro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 登录后主页满初始化服务，获取菜单、获取个人信息等等
 * </p>
 *
 * @author A
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/init")
public class IndexController {

    @Autowired
    UserInfoService userService;

    @Autowired
    CodeDataService codeDataService;

    @Autowired
    RenderPageService renderPageService;

    /**
     * 后端管理获取菜单数据
     *
     * @return
     */
    @RequestMapping(value = "/getAdminNav", method = RequestMethod.GET)
    public ResponseData<List<NavVO>> getAdminNav() {
        List<NavVO> list = userService.getAdminNavData();
        return ResponseData.success(list);
    }

    /**
     * 应用配置获取菜单数据
     *
     * @return
     */
    @RequestMapping(value = "/getSettingNav", method = RequestMethod.GET)
    public ResponseData<List<NavVO>> getSettingNav(@RequestParam Long appId) {
        List<NavVO> list = userService.getSettingNavData(appId);
        return ResponseData.success(list);
    }

    @RequestMapping(value = "/getAppPreviewNav", method = RequestMethod.GET)
    public ResponseData<List<NavVO>> getAppPreviewNav(@RequestParam Long appId) {
        if (appId == null || appId == 0) {
            ResponseData.parameterNotFound("appId");
        }
        List<NavVO> list = userService.getAppPreviewNavDataId(appId);
        return ResponseData.success(list);
    }

    /**
     * 获取用户所有有权限的导航菜单
     *
     * @return
     */
    @RequestMapping(value = "/getAppNav", method = RequestMethod.GET)
    public ResponseData<List<NavVO>> getAppNav(Long appId) {
        List<NavVO> list;
        if (appId == null || appId == 0) {
            list = renderPageService.getAppAllNavData();
        } else {
            list = renderPageService.getAppNavDataByApId(appId);
        }
        return ResponseData.success(list);
    }

    @RequestMapping(value = "/getNavByPageId", method = RequestMethod.POST)
    public ResponseData<List<NavVO>> getNavByPageIdPost(@RequestBody Map<String, String> map) {
        Long pageId = 0L;
        ClientMacro clientMacro = null;
        if (map.containsKey("pageId")) {
            pageId = Long.parseLong(map.get("pageId"));
        }
        if (StringUtils.isEmpty(pageId)) {
            return ResponseData.parameterNotFound("pageId");
        }
        if (map.containsKey("clientMacro")) {
            clientMacro = ClientMacro.init(map.get("clientMacro"));
        } else {
            return ResponseData.parameterNotFound("clientMacro");
        }
        List<NavVO> list = renderPageService.getNavDataByPageId(pageId, clientMacro);
        return ResponseData.success(list);
    }

    /**
     * 页面初始化时，获取代码表数据
     *
     * @return
     */
    @RequestMapping(value = "/getCodeData", method = RequestMethod.GET)
    public ResponseData<Object> getCodeData(Long appId) {
        if (appId == null || appId == 0) {
            return ResponseData.parameterNotFound("appId");
        }
        return ResponseData.success(codeDataService.getCodeListByAppId(appId));
    }
}
