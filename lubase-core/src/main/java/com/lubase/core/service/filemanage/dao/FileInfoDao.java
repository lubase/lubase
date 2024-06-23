package com.lubase.core.service.filemanage.dao;

import com.lubase.core.entity.SdFileInfoEntity;
import com.lubase.core.service.filemanage.model.FileInfoModel;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileInfoDao {
    @Autowired
    DataAccess dataAccess;

    /**
     * 判断是否存在相同的文件
     *
     * @param md5
     * @return
     */
    public SdFileInfoEntity existSameFile(String md5) {
        if (!StringUtils.isEmpty(md5)) {
            QueryOption queryOption = new QueryOption("sd_file_info");
            queryOption.setTableFilter(new TableFilter("md5", md5));
            DbCollection coll = dataAccess.query(queryOption);
            return coll.getGenericData(SdFileInfoEntity.class).stream().findFirst().orElse(null);
        }
        return null;
    }

    public SdFileInfoEntity getFileInfoById(String id) {
        if (!StringUtils.isEmpty(id)) {
            QueryOption queryOption = new QueryOption("sd_file_info");
            queryOption.setTableFilter(new TableFilter("id", id));
            DbCollection coll = dataAccess.query(queryOption);
            return coll.getGenericData(SdFileInfoEntity.class).stream().findFirst().orElse(null);
        }
        return null;
    }


    public DbEntity addFileInfo(FileInfoModel infoModel) {
        DbCollection coll = dataAccess.getEmptyData("sd_file_info");
        DbEntity file = coll.newEntity();
        file.put("storage_service", infoModel.getStorageService());
        file.put("file_path", infoModel.getGroupPath());
        file.put("file_name", infoModel.getNewFileName());
        file.put("original_name", infoModel.getOriginalFileName());
        file.put("file_size", infoModel.getFileSize());
        file.put("md5", infoModel.getMd5());
        file.put("ex_type", infoModel.getOriginalFileName().substring(infoModel.getOriginalFileName().lastIndexOf(".")));
        file.setState(EDBEntityState.Added);
        coll.getData().add(file);
        dataAccess.update(coll);
        return file;
    }
}
