package com.lubase.core.controller;


import com.lubase.core.model.InvokeMethodParamDTO;
import com.lubase.core.service.exportmanage.ImportService;
import com.lubase.orm.exception.WarnCommonException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/import")
public class ImportController extends BaseCommonController {

    @Autowired
    ImportService importService;

    /**
     * 获取一级页面列表导入模板
     *
     * @param map
     * @param response 返回流
     */
    @SneakyThrows
    @RequestMapping(value = "/getMainTableTemplate", method = RequestMethod.POST)
    public void getExportMainDataTemplate(@RequestBody Map<String, String> map, HttpServletResponse response) {
        //1、
        String pageId = getParam("pageId", map, true);
        String clientMacroStr = getParam("clientMacro", map, true);

        //2、导出
        try {
            importService.getExportMainPageTemplate(pageId, clientMacroStr, response);
        } catch (Exception e) {
            throw new WarnCommonException("模板导出失败：" + e.getMessage());
        }
    }

    @SneakyThrows
    @RequestMapping(value = "/importMainTableData", method = RequestMethod.POST)
    public void importMainTableData(@RequestParam String pageId, @RequestParam String clientMacroStr, @RequestParam MultipartFile file) {
        try {
            importService.importMainPageTable(pageId, clientMacroStr, file);
        } catch (Exception e) {
            throw new WarnCommonException("数据导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取子表列表导入模板
     *
     * @param methodParamModel
     * @param response
     */
    @SneakyThrows
    @RequestMapping(value = "/getSubTableTemplate", method = RequestMethod.POST)
    public void getExportSubTableDataTemplate(@RequestBody InvokeMethodParamDTO methodParamModel, HttpServletResponse response) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())
                || StringUtils.isEmpty(methodParamModel.getPageId()) || methodParamModel.getData() == null) {
            throw new WarnCommonException("methodId or pageId or data");
        }
        if (methodParamModel.getAppId() != null && methodParamModel.getAppId() > 0) {
            methodParamModel.getData().put("appId", methodParamModel.getAppId().toString());
        }
        String serialNum = getParam("serialNum", methodParamModel.getData(), true);
        String formId = getParam("formId", methodParamModel.getData(), true);
        try {
            importService.getExportSubTableTemplate(formId, serialNum, methodParamModel.getClientMacro(), response);
        } catch (Exception e) {
            throw new WarnCommonException("模板导出失败：" + e.getMessage());
        }
    }

    @SneakyThrows
    @RequestMapping(value = "/importSubTableData", method = RequestMethod.POST)
    public void importSubTableData(@RequestParam String serialNum, @RequestParam String formId, @RequestParam String clientMacroStr, @RequestParam MultipartFile file) {
        try {
            importService.importSubPageTable(formId, serialNum, clientMacroStr, file);
        } catch (Exception e) {
            throw new WarnCommonException("数据导入失败：" + e.getMessage());
        }
    }
}
