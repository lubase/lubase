package com.lubase.core.service.multiApplications.impl;

import com.lubase.core.extend.PageDataExtendService;
import com.lubase.core.service.multiApplications.ExtendFileService;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import com.lubase.orm.model.DbCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtendJarPageDataServiceImpl implements PageDataExtendService {

    @Autowired
    ExtendFileService extendFileService;

    @Override
    public DbCollection beforeReturnMainData(String pageId, DbCollection collection) {
        DbTable table = collection.getTableInfo();
        String colCode = "_extend_status";
        DbField field = new DbField();
        field.setId(colCode);
        field.setCode(field.getId());
        field.setName("加载状态");
        field.setVisible(2);
        field.setRight(2);
        field.setEleType("1");
        table.getFieldList().add(field);
        for (List<ExtendFileModel> modelList : extendFileService.getExtendFileLoadStatus().values()) {
            for (DbEntity entity : collection.getData()) {
                if (modelList != null && modelList.stream().anyMatch(f -> entity.getId().toString().equals(f.getExtendFileId()))) {
                    entity.put(colCode, "成功");
                } else {
                    entity.put(colCode, "失败");
                }
            }
        }
        colCode = "_extend_path";
        field = new DbField();
        field.setId(colCode);
        field.setCode(field.getId());
        field.setName("加载路径");
        field.setVisible(2);
        field.setRight(2);
        field.setEleType("1");
        table.getFieldList().add(field);
        for (List<ExtendFileModel> modelList : extendFileService.getExtendFileLoadStatus().values()) {
            for (DbEntity entity : collection.getData()) {
                if (modelList != null) {
                    ExtendFileModel model = modelList.stream().filter(f -> entity.getId().toString().equals(f.getExtendFileId())).findFirst().orElse(null);
                    if (model != null) {
                        entity.put(colCode, String.format("%s : %s", model.getFilePath(), model.getFileName()));
                    }
                }
            }
        }
        return collection;
    }

    @Override
    public String getPageId() {
        //页面扩展管理
        return "2022052921140314984";
    }

    @Override
    public String getDescription() {
        return "检查扩展包的加载状态l";
    }
}