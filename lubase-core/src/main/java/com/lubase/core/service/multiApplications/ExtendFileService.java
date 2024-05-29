package com.lubase.core.service.multiApplications;

import com.lubase.core.service.multiApplications.model.ExtendFileModel;

import java.util.List;
import java.util.Map;

public interface ExtendFileService {
    List<ExtendFileModel> getExtendFileListFromDb();

    List<ExtendFileModel> getExtendFileListFromDirectory(String path);

    Map<String, List<ExtendFileModel>> getExtendFileLoadStatus();
}
