package com.lubase.core.service.exportmanage;

import com.lubase.orm.model.DbCollection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {
    /**
     * DBCollection 导出excel 方法
     *
     * @param collection
     * @param response
     * @param name 文件名字，如果为空则取自 collection.getTableInfo.getTableName
     * @throws IOException
     */
    void ExportByQuery(DbCollection collection, HttpServletResponse response, String name) throws IOException;
}
