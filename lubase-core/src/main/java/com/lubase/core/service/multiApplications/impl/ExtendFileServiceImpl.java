package com.lubase.core.service.multiApplications.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.core.service.multiApplications.ExtendFileService;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;
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
        List<DbEntity> listExtendFile = getExtendFile();
        List<DbEntity> listExtendDoc = getExtendDoc(listExtendFile);
        List<ExtendFileModel> fileModelList = new ArrayList<>();
        for (DbEntity entity : listExtendDoc) {
            ExtendFileModel fileModel = new ExtendFileModel();
            DbEntity extendFileEntity = listExtendFile.stream()
                    .filter(m -> m.get("file_key").equals(entity.get("file_key")))
                    .findFirst().orElse(null);
            fileModel.setGroupId(extendFileEntity.get("group_id").toString());
            fileModel.setExtendFileId(extendFileEntity.getId().toString());
            fileModel.setFilePath(TypeConverterUtils.object2String(entity.get("file_path"), ""));
            fileModel.setFileName(TypeConverterUtils.object2String(entity.get("file_name"), ""));
            if (!StringUtils.isEmpty(fileModel.getFilePath()) && !StringUtils.isEmpty(fileModel.getFileName())) {
                fileModelList.add(fileModel);
            }
        }
        loadFileList = fileModelList;
        mapExtendFileList.put("db_setting", fileModelList);
        return fileModelList;
    }

    @Override
    public List<ExtendFileModel> getExtendFileListFromDirectory(String path) {
        File file = new File(path);
        String fixStr = ".jar";
        String defaultGroupIdPre = "com.lubase.%s";
        if (file.listFiles() == null || !file.isDirectory()) {
            return new ArrayList<>();
        }
        List<ExtendFileModel> fileModelList = new ArrayList<>();
        for (File f : file.listFiles()) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(fixStr) && f.getName().contains("-")) {
                ExtendFileModel fileModel = new ExtendFileModel();
                fileModel.setGroupId(String.format(defaultGroupIdPre, f.getName().split("-")[0]));
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

    List<DbEntity> getExtendFile() {
        QueryOption qoFile = new QueryOption("ss_extend_file", 0, 0);
        qoFile.setFixField("file_key,scope,group_id");
        TableFilterWrapper wrapper = new TableFilterWrapper(true);
        wrapper.eq("file_type", "1").eq("category", "1").eq("delete_tag", "0");
        qoFile.setTableFilter(wrapper.build());
        return dataAccess.query(qoFile).getData();
    }

    List<DbEntity> getExtendDoc(List<DbEntity> data) {
        List<String> fileKey = data.stream().map(m -> m.get("file_key").toString()).collect(Collectors.toList());
        String fileKeys = String.join(",", fileKey);
        QueryOption qoDoc = new QueryOption("sd_upload_file", 0, 0);
        qoDoc.setFixField("file_name,file_path,md5,file_key");
        TableFilterWrapper wrapperDoc = new TableFilterWrapper(true);
        wrapperDoc.in("file_key", fileKeys);
        qoDoc.setTableFilter(wrapperDoc.build());
        DbCollection collDoc = dataAccess.query(qoDoc);
        return collDoc.getData();
    }
}
