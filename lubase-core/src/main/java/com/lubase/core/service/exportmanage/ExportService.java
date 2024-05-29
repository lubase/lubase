package com.lubase.core.service.exportmanage;

import com.lubase.orm.model.DbCollection;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExportService {

    void ExportByQuery(DbCollection collection, HttpServletResponse response, String name) throws IOException;
}
