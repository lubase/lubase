package com.lubase.core.service.exportmanage;

import com.lubase.orm.model.DbCollection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {
    /**
     * DBCollection 导出excel 方法
     *
     * @param tableIdentityCode  列表标识，一级页面取页面id，子表取serialNum
     * @param collection
     * @param response
     * @param name 文件名字，如果为空则取自 collection.getTableInfo.getTableName
     * @throws IOException
     */
    void ExportByQuery(String tableIdentityCode, DbCollection collection, HttpServletResponse response, String name) throws IOException;
}
