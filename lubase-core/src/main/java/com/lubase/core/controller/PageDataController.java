package com.lubase.core.controller;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.AppHolderService;
import com.lubase.model.DbEntity;
import com.lubase.core.model.GetMainDataParamDTO;
import com.lubase.core.service.*;
import com.lubase.core.service.exportmanage.ExportService;
import com.lubase.core.model.DisplayListVO;
import com.lubase.core.model.PageInfoVO;
import com.lubase.core.response.ResponseData;
import com.lubase.core.util.ClientMacro;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 模板页渲染服务
 *
 * @author A
 */
@RestController
@RequestMapping("/page")
public class PageDataController {

    @Autowired
    PageDataService pageDataService;

    @Autowired
    RenderPageService renderPageService;

    @Autowired
    RenderTableService renderTableService;

    @Autowired
    RenderTreeService renderTreeService;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    PersonalizationDataService personalizationDataService;

    @Autowired
    ExportService exportService;

    @RequestMapping(value = "/getExtendEleDisplayList", method = RequestMethod.GET)
    public ResponseData<List<DbEntity>> getExtendEleDisplayList() {
        return ResponseData.success(renderPageService.getExtendDisplayType());
    }

    /**
     * 根据页面代码获取页面的配置信息
     *
     * @param pageId 页面代码
     * @return 页面的配置信息
     */
    @RequestMapping(value = "/getPageInfo", method = RequestMethod.GET)
    public ResponseData<PageInfoVO> getPageInfo(@RequestParam String pageId) {
        if (StringUtils.isEmpty(pageId)) {
            return ResponseData.parameterNotFound("pageId");
        }
        return ResponseData.success(renderPageService.getPageInfo(pageId));
    }

    /**
     * 获取页面中左侧区域的数据
     *
     * @param map 包含参数
     *            code        页面代码
     *            customParam 页面管理中设置的自定义参数，此处应为对象序列化后的字符串
     * @return
     */
    @RequestMapping(value = "/getLeftData", method = RequestMethod.POST)
    public ResponseData<List<DbEntity>> getLeftData(@RequestBody Map<String, String> map) {
        String pageId = "";
        ClientMacro clientMacro = null;
        if (map.containsKey("pageId")) {
            pageId = map.get("pageId");
        }
        if (map.containsKey("clientMacro")) {
            clientMacro = ClientMacro.init(map.get("clientMacro"));
        } else {
            return ResponseData.parameterNotFound("clientMacro");
        }
        if (StringUtils.isEmpty(pageId)) {
            return ResponseData.parameterNotFound("pageId");
        }
        return ResponseData.success(renderTreeService.getTreeDataByPageId(pageId, clientMacro));
    }

    @RequestMapping(value = "/getTabData", method = RequestMethod.POST)
    public ResponseData<List<DbEntity>> getTabData(@RequestBody Map<String, String> map) {
        return getLeftData(map);
    }

    /**
     * @param map 包含参数
     *            code        页面代码
     *            queryParam  查询参数，包含了页码、数量、排序等
     *            clientMacro 客户端宏变量
     * @return
     */
    @RequestMapping(value = "/getMainData", method = RequestMethod.POST)
    public ResponseData<DbCollection> getMainData(@RequestBody GetMainDataParamDTO paramDTO) {
        if (StringUtils.isEmpty(paramDTO.getPageId()) || StringUtils.isEmpty(paramDTO.getQueryParam()) || StringUtils.isEmpty(paramDTO.getClientMacro())) {
            return ResponseData.parameterNotFound("pageId and queryParam");
        }
        ClientMacro clientMacro = ClientMacro.init(paramDTO.getClientMacro());
        return ResponseData.success(renderTableService.getGridDataByPageId(paramDTO.getPageId(), clientMacro, paramDTO.getSearchParam(), paramDTO.getQueryParam(), paramDTO.getFullTextSearch()));
    }

    @RequestMapping(value = "/getStatisticInfoData", method = RequestMethod.POST)
    public ResponseData<DbCollection> getStatisticInfoData(@RequestBody Map<String, String> map) {
        String pageId = "", queryParam = "", rowValue = "", colValue = "";
        ClientMacro clientMacro = null;
        if (map.containsKey("pageId")) {
            pageId = map.get("pageId");
        }
        if (StringUtils.isEmpty(pageId)) {
            return ResponseData.parameterNotFound("pageId");
        }
        if (map.containsKey("queryParam")) {
            queryParam = map.get("queryParam");
        }
        if (map.containsKey("clientMacro")) {
            clientMacro = ClientMacro.init(map.get("clientMacro"));
        } else {
            return ResponseData.parameterNotFound("clientMacro");
        }
        if (map.containsKey("rowValue")) {
            rowValue = map.get("rowValue");
        }
        if (map.containsKey("colValue")) {
            colValue = map.get("colValue");
        }
        return ResponseData.success(renderTableService.getStatisticsInfo(pageId, queryParam, clientMacro, rowValue, colValue));
    }

    @SneakyThrows
    @RequestMapping(value = "/exportStatisticInfoData", method = RequestMethod.POST)
    public void exportStatisticInfoData(@RequestBody Map<String, String> map, HttpServletResponse response) {
        String pageId = "", queryParam = "", rowValue = "", colValue = "";
        ClientMacro clientMacro = null;
        if (map.containsKey("pageId")) {
            pageId = map.get("pageId");
        }
        if (StringUtils.isEmpty(pageId)) {
            throw new InvokeCommonException("pageId  not found");
        }
        if (map.containsKey("queryParam")) {
            queryParam = map.get("queryParam");
        }
        if (map.containsKey("clientMacro")) {
            clientMacro = ClientMacro.init(map.get("clientMacro"));
        } else {
            throw new InvokeCommonException("clientMacro  not found");
        }
        if (map.containsKey("rowValue")) {
            rowValue = map.get("rowValue");
        }
        if (map.containsKey("colValue")) {
            colValue = map.get("colValue");
        }
        DbCollection collection = renderTableService.getStatisticsInfoNoPaging(pageId, queryParam, clientMacro, rowValue, colValue);
        String name = collection.getTableInfo().getName();
        exportService.ExportByQuery(collection, response, name);
    }

    /**
     * 个性化页面保存
     *
     * @param displayListVO
     * @return
     */
    @RequestMapping(value = "/saveDisplayList", method = RequestMethod.POST)
    public ResponseData<String> saveDisplayList(@RequestBody DisplayListVO displayListVO) {
        if (StringUtils.isEmpty(displayListVO.getPageId())) {
            return ResponseData.parameterNotFound("pageId");
        }
        Long pageId, userId;
        pageId = displayListVO.getPageId();
        userId = appHolderService.getUser().getId();
        DisplayListVO updateResult = personalizationDataService.saveDisplayColumn(pageId, userId, displayListVO.getColumnIds(), displayListVO.getLockColumnCount());
        return ResponseData.success(JSON.toJSONString(updateResult));
    }

    /**
     * @param displayListVO
     * @return
     */
    @RequestMapping(value = "/saveColumnWidth", method = RequestMethod.POST)
    public ResponseData<String> saveColumnWidth(@RequestBody DisplayListVO displayListVO) {
        if (StringUtils.isEmpty(displayListVO.getPageId()) || StringUtils.isEmpty(displayListVO.getColumnWidthSetting())) {
            return ResponseData.parameterNotFound("pageId and columnWidthSetting");
        }
        Long pageId, userId;
        pageId = displayListVO.getPageId();
        userId = appHolderService.getUser().getId();
        DisplayListVO updateResult = personalizationDataService.saveColumnWidthSetting(pageId, userId, displayListVO.getColumnWidthSetting());
        return ResponseData.success(JSON.toJSONString(updateResult));
    }
}
