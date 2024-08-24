package com.lubase.core.controller;


import com.lubase.core.model.InvokeMethodParamDTO;
import com.lubase.core.service.RenderFormService;
import com.lubase.core.service.RenderTableService;
import com.lubase.core.service.exportmanage.ExportService;
import com.lubase.core.util.ClientMacro;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/export")
public class ExportController extends BaseCommonController {

    @Autowired
    ExportService exportService;
    @Autowired
    RenderTableService renderTableService;
    @Autowired
    RenderFormService renderFormService;

    /**
     * 导出主列表数据。支持导出当前页和全部数据
     *
     * @param map      pageId,queryParam,clientMacro,name
     * @param response 返回流
     */
    @SneakyThrows
    @RequestMapping(value = "/mainData", method = RequestMethod.POST)
    public void exportMainData(@RequestBody Map<String, String> map, HttpServletResponse response) {
        String pageId = "", queryParam = "", name = "", searchParam = "", fullTextSearch = "";

        //1、 获取列表数据
        pageId = getParam("pageId", map, true);
        String clientMacroStr = getParam("clientMacro", map, true);
        searchParam = getParam("searchParam", map, false);
        queryParam = getParam("queryParam", map, false);
        fullTextSearch = getParam("fullTextSearch", map, false);
        ClientMacro clientMacro = ClientMacro.init(clientMacroStr);
        String allData = getParam("allData", map, false);

        DbCollection collection;
        if (allData.equals("1")) {
            collection = renderTableService.getGridAllDataByPageId(pageId, clientMacro, searchParam, queryParam, fullTextSearch);
        } else {
            collection = renderTableService.getGridDataByPageId(pageId, clientMacro, searchParam, queryParam, fullTextSearch);
        }

        //2、导出
        try {
            name = getParam("name", map, false);
            exportService.ExportByQuery(collection, response, name);
        } catch (Exception e) {
            throw new WarnCommonException("文件导出失败：" + e.getMessage());
        }
    }

    /**
     * 导出子表数据
     *
     * @param methodParamModel
     * @param response
     */
    @SneakyThrows
    @RequestMapping(value = "/subTableData", method = RequestMethod.POST)
    public void exportSubTableData(@RequestBody InvokeMethodParamDTO methodParamModel, HttpServletResponse response) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())
                || StringUtils.isEmpty(methodParamModel.getPageId()) || methodParamModel.getData() == null) {
            throw new WarnCommonException("methodId or pageId or data");
        }
        if (methodParamModel.getAppId() != null && methodParamModel.getAppId() > 0) {
            methodParamModel.getData().put("appId", methodParamModel.getAppId().toString());
        }
        String allData = getParam("allData", methodParamModel.getData(), false);
        DbCollection collection;
        if (allData.equals("1")) {
            collection = renderFormService.getFormChildAllTableData(methodParamModel.getData()).getDbCollection();
        } else {
            collection = renderFormService.getFormChildTableData(methodParamModel.getData()).getDbCollection();
        }

        try {
            String name = collection.getTableInfo().getName();
            exportService.ExportByQuery(collection, response, name);
        } catch (Exception e) {
            throw new WarnCommonException("文件导出失败：" + e.getMessage());
        }
    }

    /**
     * 导出统计模板详情数据
     *
     * @param map
     * @param response
     */
    @SneakyThrows
    @RequestMapping(value = "/statisticInfoData", method = RequestMethod.POST)
    public void exportStatisticInfoData(@RequestBody Map<String, String> map, HttpServletResponse response) {
        String pageId = getParam("pageId", map, true);
        String clientMacroStr = getParam("clientMacro", map, true);
        ClientMacro clientMacro = ClientMacro.init(clientMacroStr);
        String searchParam = getParam("searchParam", map, false);
        String rowValue = getParam("rowValue", map, false);
        String colValue = getParam("colValue", map, false);

        DbCollection collection = renderTableService.getStatisticsInfoNoPaging(pageId, searchParam, clientMacro, rowValue, colValue);

        try {
            String name = collection.getTableInfo().getName();
            exportService.ExportByQuery(collection, response, name);
        } catch (Exception e) {
            throw new WarnCommonException("文件导出失败：" + e.getMessage());
        }
    }
}
