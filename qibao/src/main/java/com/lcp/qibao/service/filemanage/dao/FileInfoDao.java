package com.lcp.qibao.service.filemanage.dao;

import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.EDBEntityState;
import com.lcp.qibao.auto.entity.SdFileInfoEntity;
import com.lcp.qibao.service.filemanage.model.FileInfoModel;
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
