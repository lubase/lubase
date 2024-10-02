package com.lubase.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.constant.CommonConstant;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.exception.NoRightAccessFuncException;
import com.lubase.core.extend.GroupPageExtendService;
import com.lubase.core.extend.service.PageServiceAdapter;
import com.lubase.core.model.*;
import com.lubase.core.service.AppFuncDataService;
import com.lubase.core.service.AppNavDataService;
import com.lubase.core.service.RenderPageService;
import com.lubase.core.service.RenderTableExtendService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.model.UserRightInfo;
import com.lubase.core.util.ClientMacro;
import com.lubase.core.util.MultilingualToolUtil;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.constant.CommonConst;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.ServerMacroService;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RenderPageServiceImpl implements RenderPageService {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    AppHolderService appHolderService;

    @Autowired
    UserRightService userRightService;

    @Autowired
    AppNavDataService appNavDataService;
    @Autowired
    AppFuncDataService appFuncDataService;
    @Autowired
    PageServiceAdapter pageServiceAdapter;
    @Autowired
    RenderTableExtendService gridDataExtendService;

    @Autowired
    ServerMacroService serverMacroService;
    @Autowired
    MultilingualToolUtil multilingualToolUtil;

    @Override
    public List<NavVO> getAppAllNavData() {
        LoginUser user = appHolderService.getUser();
        List<Long> appList = userRightService.getUserAllApp(user.getId());
        List<NavVO> allNavVOList = new ArrayList<>();
        List<DbEntity> allAppInfoList = appNavDataService.getAllAppInfo();
        for (Long appId : appList) {
            //排除后端应用和前端独立部署的应用
            if (CommonConstant.SYSTEM_APP_ID.equals(appId)) {
                continue;
            }
            DbEntity appInfo = allAppInfoList.stream().filter(a -> a.getId().equals(appId)).findFirst().orElse(null);
            if (appInfo == null || TypeConverterUtils.object2String(appInfo.get("system_app_tag"), "0").equals("1")) {
                continue;
            }
            //前端主框架部署不显示菜单0:主框架  1：独立部署
            if (TypeConverterUtils.object2Integer(appInfo.get("web_deploy_type"), 0).equals(1)) {
                continue;
            }
            List<NavVO> allRightNavVOList = appNavDataService.getNavDataByAppId(appId);
            multilingualToolUtil.processCurrentLanguageForNav(allRightNavVOList, appId.toString());
            allNavVOList.addAll(allRightNavVOList);
        }
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        List<NavVO> allRightNavVOList = new ArrayList<>();
        for (NavVO navVO : allNavVOList) {
            if (userRightService.checkFuncRight(rightInfo, navVO.getId())) {
                allRightNavVOList.add(navVO);
            }
        }

        return allRightNavVOList;
    }

    @Override
    public List<NavVO> getAppNavDataByApId(Long appId) {
        LoginUser user = appHolderService.getUser();
        List<NavVO> allNavVOList = appNavDataService.getNavDataByAppId(appId);
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        List<NavVO> allRightNavVOList = new ArrayList<>();
        for (NavVO navVO : allNavVOList) {
            if (userRightService.checkFuncRight(rightInfo, navVO.getId())) {
                allRightNavVOList.add(navVO);
            }
        }
        multilingualToolUtil.processCurrentLanguageForNav(allRightNavVOList, appId.toString());
        return allRightNavVOList;
    }


    @SneakyThrows
    @Override
    public List<NavVO> getNavDataByPageId(Long pageId, ClientMacro clientMacro) {
        if (pageId == null) {
            throw new ParameterNotFoundException("pageId");
        }
        LoginUser user = appHolderService.getUser();
        List<NavVO> allNavVOList = appNavDataService.getNavDataByPageId(pageId);
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        List<NavVO> allRightNavVOList = new ArrayList<>();
        for (NavVO navVO : allNavVOList) {
            if (userRightService.checkFuncRight(rightInfo, navVO.getId())) {
                allRightNavVOList.add(navVO);
            }
        }
        GroupPageExtendService extendService = pageServiceAdapter.getGroupPageExtendService(pageId.toString());
        if (extendService != null) {
            allRightNavVOList = extendService.beforeReturnChildPageData(pageId.toString(), allRightNavVOList, clientMacro);
        }
        //对allRightNavVOList 按OrderId属性进行升序排序
        allRightNavVOList.sort((o1, o2) -> {
            Integer o1OrderId = o1.getOrderId();
            Integer o2OrderId = o2.getOrderId();
            return o1OrderId.compareTo(o2OrderId);
        });
        return allRightNavVOList;
    }

    @SneakyThrows
    @Override
    public NavVO getNavInfoByPageId(Long pageId) {
        if (pageId == null) {
            throw new ParameterNotFoundException("pageId");
        }
        LoginUser user = appHolderService.getUser();
        NavVO navVO = appNavDataService.getNavInfoByPageId(pageId);
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        if (userRightService.checkFuncRight(rightInfo, navVO.getId())) {
            return navVO;
        } else {
            return null;
        }
    }

    @Override
    public List<DbEntity> getExtendDisplayType(Integer webVersion) {
        if (webVersion == null) {
            webVersion = 1;
        }
        QueryOption queryOption = new QueryOption("ss_web_component");
        queryOption.setFixField("ele_type,ele_distype,name,load_from_file,extend_code");
        queryOption.setBuildLookupField(false);
        queryOption.setTableFilter(new TableFilter("web_version", webVersion, EOperateMode.Equals));
        return dataAccess.queryAllData(queryOption).getData();
    }


    @SneakyThrows
    @Override
    public PageInfoVO getPageInfo(String pageId) {
        PageInfoVO pageInfoVO = new PageInfoVO();
        SsPageEntity pageEntity = appFuncDataService.getPageById(pageId);
        if (pageEntity == null) {
            return pageInfoVO;
        }
        LoginUser user = appHolderService.getUser();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        // 暂时去掉权限判断。需增加 应用管理员对应用内全部权限功能后再开启
        // 超级管理员不授权，公共页面不受权限控制
        boolean rightControl = !rightInfo.getIsAppAdministrator() && !rightInfo.getIsSupperAdministrator() && !pageEntity.getType().equals(EPageType.CommonPage.getType());
        if (rightControl && !userRightService.checkFuncRight(rightInfo, Long.parseLong(pageId))) {
            throw new NoRightAccessFuncException();
        }
        List<ButtonVO> allButtonList = getButtonListByPageId(pageEntity.getId());
        List<ButtonVO> rightButtonList = new ArrayList<>();
        for (ButtonVO buttonVO : allButtonList) {
            if (userRightService.checkFuncRight(rightInfo, Long.parseLong(buttonVO.getId()))) {
                rightButtonList.add(buttonVO);
            }
        }
        multilingualToolUtil.processCurrentLanguageForButton(rightButtonList, pageEntity.getId().toString());
        pageInfoVO.setBtns(rightButtonList);
        pageInfoVO.setId(pageEntity.getId());
        pageInfoVO.setName(pageEntity.getPage_name());
        pageInfoVO.setDes(pageEntity.getDescription());
        pageInfoVO.setTmp(pageEntity.getMaster_page());
        SearchVO searchVO = getSearchVO(pageEntity);
        pageInfoVO.setSearch(searchVO);
        pageInfoVO.setTreeInfo(pageEntity.getTree_info());
        pageInfoVO.setGridInfo(pageEntity.getGrid_info());
        pageInfoVO.setCustomParam(pageEntity.getCustom_params());
        pageInfoVO.setBannerSetting(pageEntity.getBanner_setting());
        pageInfoVO.setFormSetting(pageEntity.getForm_setting());

        gridDataExtendService.beforeReturnPageInfo(pageEntity, pageInfoVO);
        return pageInfoVO;
    }

    List<ButtonVO> getButtonListByPageId(Long pageId) {
        List<DbEntity> buttonEntities = appFuncDataService.getButtonListByPageId(pageId);
        List<ButtonVO> buttonVOS = new ArrayList<>();
        for (DbEntity entity : buttonEntities) {
            ButtonVO buttonVO = new ButtonVO();
            buttonVO.setCode(entity.getId().toString());
            buttonVO.setId(entity.getId().toString());
            buttonVO.setName(TypeConverterUtils.object2String(entity.get("button_name")));
            buttonVO.setBtnType(TypeConverterUtils.object2String(entity.get("button_type")));
            buttonVO.setDisType(TypeConverterUtils.object2String(entity.get("display_type")));
            buttonVO.setOrderId(TypeConverterUtils.object2Integer(entity.get("order_id")));
            buttonVO.setGroupDes(TypeConverterUtils.object2String(entity.get("group_des")));
            //以下是否是客户端渲染参数
            buttonVO.setNavAddress(TypeConverterUtils.object2String(entity.get("nav_address")));
            buttonVO.setLinkColumn(TypeConverterUtils.object2String(entity.get("link_column")));
            buttonVO.setRenderSetting(TypeConverterUtils.object2String(entity.get("render_setting")));
            buttonVOS.add(buttonVO);
        }
        return buttonVOS;
    }

    @SneakyThrows
    private SearchVO getSearchVO(SsPageEntity pageEntity) {
        if (StringUtils.isEmpty(pageEntity.getSearch_filter())) {
            return new SearchVO();
        }
        //TODO:兼容数据
        if (pageEntity.getSearch_filter().contains("FilterValue")) {
            return new SearchVO();
        }

        String mainTableCode;
        QueryOption queryGrid = JSON.parseObject(pageEntity.getGrid_query(), QueryOption.class);
        if (queryGrid != null && !StringUtils.isEmpty(queryGrid.getTableName())) {
            mainTableCode = queryGrid.getTableName();
        } else {
            throw new WarnCommonException("表格数据源配置错误，请设置TableName属性");
        }

        SearchVO searchVO = new SearchVO();
        List<SearchCondition> list = JSON.parseArray(pageEntity.getSearch_filter(), SearchCondition.class);
        if (list == null || list.size() == 0) {
            return searchVO;
        }
        String usedField = "";
        for (SearchCondition condition : list) {
            usedField += "," + condition.getColumnCode();
            //如果defaultValue属性不为空，则给此属性用服务端宏变量重新赋值
            if (condition.getDefaultValue() != null && condition.getDefaultValue().startsWith(ClientMacro.serverMacroPre)) {
                condition.setDefaultValue(serverMacroService.getServerMacroByKey(condition.getDefaultValue()));
            }
            if (condition.getDefaultValueName() != null && condition.getDefaultValueName().startsWith(ClientMacro.serverMacroPre)) {
                condition.setDefaultValueName(serverMacroService.getServerMacroByKey(condition.getDefaultValueName()));
            }
        }
        usedField = usedField.substring(1).toLowerCase();
        if (StringUtils.isEmpty(usedField)) {
            return searchVO;
        }
        HashMap<String, DbField> fieldBOHashMap = new HashMap<>();
        try {
            QueryOption queryOption = new QueryOption(mainTableCode);
            queryOption.setFixField(usedField);
            DbCollection collection = dataAccess.queryFieldList(queryOption);

            List<DbField> fieldList = collection.getTableInfo().getFieldList();
            for (DbField field : fieldList) {
                if (field.isPrimaryKey() && !usedField.contains("id")) {
                    continue;
                }
                // 需要字段名字进行完全匹配，方便前端进行标准控件渲染
                field.setCode(field.getCode().replace(CommonConst.REF_FIELD_SEPARATOR, "."));
                fieldBOHashMap.put(field.getCode(), field);
            }
        } catch (Exception ex) {
            log.error(String.format("页面搜索区域配置错误，页面：%s %s，搜索条件：%s", pageEntity.getId(), pageEntity.getPage_name(), pageEntity.getSearch_filter()), ex.getMessage());
        }
        multilingualToolUtil.processCurrentLanguageForSearch(fieldBOHashMap, pageEntity.getApp_id().toString());
        searchVO.setFieldInfo(fieldBOHashMap);
        searchVO.setFilter(list);
        return searchVO;
    }
}
