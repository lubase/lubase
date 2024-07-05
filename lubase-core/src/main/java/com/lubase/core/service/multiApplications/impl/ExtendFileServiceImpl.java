package com.lubase.core.service.multiApplications.impl;

import com.lubase.core.service.multiApplications.ExtendFileService;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExtendFileServiceImpl implements ExtendFileService {
    @Autowired
    DataAccess dataAccess;

    List<ExtendFileModel> loadFileList = null;

    Map<String, List<ExtendFileModel>> mapExtendFileList = new HashMap<>();

    @Override
    public List<ExtendFileModel> getExtendFileListFromDb() {
        return null;
    }

    @Override
    public List<ExtendFileModel> getExtendFileListFromDirectory(String path) {
        File file = new File(path);
        String fixStr = ".jar";
        String defaultGroupIdPre = "com.lubase.%s";
        if (file.listFiles() == null || !file.isDirectory()) {
            return new ArrayList<>();
        }
        Map<String, String> map = getExtendFilePackage();
        List<ExtendFileModel> fileModelList = new ArrayList<>();
        for (File f : file.listFiles()) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(fixStr) && f.getName().contains("-")) {
                ExtendFileModel fileModel = new ExtendFileModel();
                if (map.containsKey(f.getName())) {
                    fileModel.setGroupId(map.get(f.getName()));
                } else {
                    fileModel.setGroupId(String.format(defaultGroupIdPre, f.getName().split("-")[0]));
                }
                fileModel.setFilePath(f.getParent());
                fileModel.setFileName(f.getName());
                fileModelList.add(fileModel);
            }
        }
        loadFileList = fileModelList;
        mapExtendFileList.put(path, fileModelList);
        return fileModelList;
    }

    @Override
    public Map<String, List<ExtendFileModel>> getExtendFileLoadStatus() {
        return mapExtendFileList;
    }

    Map<String, String> getExtendFilePackage() {
        QueryOption qoFile = new QueryOption("sd_extend_file", 0, 0);
        qoFile.setFixField("file_name,package_name");
        List<DbEntity> list = dataAccess.query(qoFile).getData();
        Map<String, String> map = new HashMap<>();
        for (DbEntity entity : list) {
            map.put(entity.get("file_name").toString(), entity.get("package_name").toString());
        }
        return map;
    }
}
