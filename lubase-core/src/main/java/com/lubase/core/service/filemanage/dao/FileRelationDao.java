package com.lubase.core.service.filemanage.dao;

import com.lubase.core.service.filemanage.model.FileInfoVO;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FileRelationDao {
    @Autowired
    DataAccess dataAccess;

    public DbEntity getFileRelationById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        DbCollection coll = dataAccess.queryById("sd_file_relation", Long.parseLong(id));
        if (coll.getData().size() == 1) {
            return coll.getData().get(0);
        } else {
            return null;
        }
    }

    public Boolean deleteFileRelation(DbEntity entity) {
        DbCollection collection = dataAccess.getEmptyData("sd_file_relation");
        entity.setState(EDBEntityState.Deleted);
        collection.getData().add(entity);
        return dataAccess.update(collection) > 0;
    }

    public DbEntity addFileRelation(FileInfoVO fileInfo, String fileInfoId) {
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        TableFilter filter = filterWrapper.eq("data_id", fileInfo.getDataId()).
                eq("data_column_tag", fileInfo.getFileKey()).eq("file_info_id", fileInfoId).build();
        QueryOption queryOption = new QueryOption("sd_file_relation");
        queryOption.setTableFilter(filter);
        DbCollection collExist = dataAccess.query(queryOption);
        if (collExist.getData().size() == 0) {
            DbCollection coll = dataAccess.getEmptyData("sd_file_relation");
            DbEntity fileRelation = coll.newEntity();
            fileRelation.put("data_id", fileInfo.getDataId());
            fileRelation.put("data_column_tag", fileInfo.getFileKey());
            fileRelation.put("file_info_id", fileInfoId);
            fileRelation.put("original_name", fileInfo.getOriginalFileName());
            fileRelation.put("app_id", fileInfo.getAppId());
            fileRelation.setState(EDBEntityState.Added);
            coll.getData().add(fileRelation);
            dataAccess.update(coll);
            return fileRelation;
        } else {
            return collExist.getData().get(0);
        }
    }

    /**
     * 复制文件
     *
     * @param fromDataId
     * @param fromColumnTag
     * @param toDataId
     * @param toColumnTag
     * @return
     */
    public Integer copyFileRelation(String fromDataId, String fromColumnTag, String toDataId, String toColumnTag) {
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        TableFilter filter = filterWrapper.eq("data_id", fromDataId).
                eq("data_column_tag", fromColumnTag).build();
        QueryOption queryOption = new QueryOption("sd_file_relation");
        queryOption.setTableFilter(filter);
        DbCollection collExist = dataAccess.queryAllData(queryOption);

        for (DbEntity entity : collExist.getData()) {
            entity.setState(EDBEntityState.Added);
            entity.setId(null);
            entity.put("data_id", toDataId);
            entity.put("data_column_tag", toColumnTag);
        }
        dataAccess.update(collExist);
        return 1;
    }

    public Boolean deleteFileRelation(String appId, String dataId, String fileKey, String fileInfoId) {
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        TableFilter filter = filterWrapper.eq("app_id", appId).eq("data_id", dataId).
                eq("data_column_tag", fileKey).eq("file_info_id", fileInfoId).build();
        QueryOption queryOption = new QueryOption("sd_file_relation");
        queryOption.setTableFilter(filter);
        DbCollection collExist = dataAccess.query(queryOption);
        if (collExist.getData().size() == 1) {
            collExist.getData().get(0).setState(EDBEntityState.Deleted);
            return dataAccess.update(collExist) > 0;
        }
        return false;
    }


}
