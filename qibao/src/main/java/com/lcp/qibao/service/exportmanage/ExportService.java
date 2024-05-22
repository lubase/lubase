package com.lcp.qibao.service.exportmanage;

import com.lcp.core.model.DbCollection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {

    void ExportByQuery(DbCollection collection, HttpServletResponse response, String name) throws IOException;
}
