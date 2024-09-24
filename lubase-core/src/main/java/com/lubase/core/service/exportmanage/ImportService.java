package com.lubase.core.service.exportmanage;


import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ImportService {
    /**
     * 导出一级页面excel模板
     *
     * @param pageId
     * @param response
     */
    void getExportMainPageTemplate(String pageId, String clientMacroStr, HttpServletResponse response);

    /**
     * 导入主表数据
     *
     * @param pageId
     * @param file
     */
    boolean importMainPageTable(String pageId, String clientMacroStr, MultipartFile file);


    /**
     * 导出子表excel模板
     *
     * @param serialNo
     * @param response
     */
    void getExportSubTableTemplate(String formId, String serialNo, String clientMacroStr, HttpServletResponse response);

    /**
     * 导入子表列表
     *
     * @param formId
     * @param serialNo
     * @param clientMacroStr
     * @param file
     */
    boolean importSubPageTable(String formId, String serialNo, String clientMacroStr, MultipartFile file);
}
