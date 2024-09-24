package com.lubase.core.extend;

import javax.servlet.http.HttpServletResponse;

/**
 * 导入扩展服务
 */
public interface ImportExtendService {
    /**
     * 导出一级页面excel模板
     *
     * @param pageId
     * @param response
     */
    void exportMainPageTemplate(String pageId, HttpServletResponse response);

    /**
     * 导出子表excel模板
     *
     * @param serialNo
     * @param response
     */
    void exportSubpageTemplate(String serialNo, HttpServletResponse response);

}
