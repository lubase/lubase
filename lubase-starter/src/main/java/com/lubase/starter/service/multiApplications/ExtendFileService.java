package com.lubase.starter.service.multiApplications;

import com.lubase.starter.service.multiApplications.model.ExtendFileModel;

import java.util.List;
import java.util.Map;

public interface ExtendFileService {
    List<ExtendFileModel> getExtendFileListFromDb();

    List<ExtendFileModel> getExtendFileListFromDirectory(String path);

    Map<String, List<ExtendFileModel>> getExtendFileLoadStatus();
}
