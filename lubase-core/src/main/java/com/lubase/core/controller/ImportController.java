package com.lubase.core.controller;


import com.lubase.core.model.InvokeMethodParamDTO;
import com.lubase.core.response.ResponseData;
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

    /**
     * @param pageId         页面id
     * @param clientMacroStr 客户端宏变量
     * @param file           导入文件
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "/importMainTableData", method = RequestMethod.POST)
    public ResponseData<Boolean> importMainTableData(@RequestParam String pageId, @RequestParam String clientMacroStr, @RequestParam MultipartFile file) {
        try {
            return ResponseData.success(importService.importMainPageTable(pageId, clientMacroStr, file));
        } catch (Exception e) {
            return ResponseData.error("数据导入失败：" + e.getMessage());
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

    /**
     * 导入子表列表
     *
     * @param pageId         页面id
     * @param serialNum      子表id
     * @param formId         表单id
     * @param dataId         主数据id
     * @param clientMacroStr 客户端宏变量
     * @param file           附件
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "/importSubTableData", method = RequestMethod.POST)
    public ResponseData<Boolean> importSubTableData(@RequestParam String pageId, @RequestParam String serialNum, @RequestParam String formId, @RequestParam String dataId, @RequestParam String clientMacroStr, @RequestParam MultipartFile file) {
        try {
            return ResponseData.success(importService.importSubPageTable(pageId, formId, serialNum, dataId, clientMacroStr, file));
        } catch (Exception e) {
            return ResponseData.error("数据导入失败：" + e.getMessage());
        }
    }
}
