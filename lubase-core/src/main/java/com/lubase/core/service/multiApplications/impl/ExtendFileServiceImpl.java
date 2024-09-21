package com.lubase.core.service.multiApplications.impl;

import com.lubase.core.service.multiApplications.ExtendFileService;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.service.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExtendFileServiceImpl implements ExtendFileService {
    @Autowired
    DataAccess dataAccess;

    /**
     * 扩展包路径，如果配置后则优先从此路径获取数据
     */
    @Value("${lubase.extend-path:}")
    String extendPath;

    List<ExtendFileModel> loadFileList = null;

    Map<String, List<ExtendFileModel>> mapExtendFileList = new HashMap<>();

    @Override
    public String getExtendPath() {
        String jarPath = "";

        if (StringUtils.isEmpty(extendPath)) {
            jarPath = Path.of(System.getProperty("user.dir"), "extend").toString();
            // 路径拼接
        } else {
            jarPath = extendPath;
        }
        return jarPath;
    }

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
                fileModel.setLastModifiedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(f.lastModified())));
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
