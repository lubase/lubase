package com.lubase.starter.service.exportmanage;

import com.lubase.core.model.DbCollection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {

    void ExportByQuery(DbCollection collection, HttpServletResponse response, String name) throws IOException;
}
