package com.lcp.qibao.service.filemanage.service;

import com.lcp.core.QueryOption;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.TableFilterWrapper;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.EDBEntityState;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MoveOldFIleService {

    @Value("${custom.file-upload.storage-type:localFileStorageService}")
    private String storageType;
    @Autowired
    DataAccess dataAccess;
    @Autowired
    Map<String, FileStorageService> fileStorageServices;

    @SneakyThrows
    FileStorageService getStorageService() {
        if (fileStorageServices.containsKey(storageType)) {
            return fileStorageServices.get(storageType);
        } else {
            throw new WarnCommonException("未找到FileStorageService服务，服务名字：" + storageType);
        }
    }

    /**
     * 迁移全部文件
     */
    public Integer moveFileToFileInfo() {
        DbCollection collOld = getAllOldFile();
        Map<String, DbEntity> mapMd5 = new HashMap<>();
        for (DbEntity entity : collOld.getData()) {
            String md5 = entity.get("md5").toString();
            if (mapMd5.containsKey(md5)) {
                continue;
            }
            mapMd5.put(md5, entity);
        }
        DbCollection collNewFileInfo = dataAccess.getEmptyData("sd_file_info");
        for (String md5 : mapMd5.keySet()) {
            DbEntity entity = collNewFileInfo.newEntity();
            DbEntity oldEntity = mapMd5.get(md5);
            entity.put("file_path", oldEntity.get("file_path").toString());
            entity.put("file_name", oldEntity.get("file_name").toString());
            entity.put("original_name", oldEntity.get("file_name").toString());
            entity.put("ex_type", oldEntity.get("ex_type").toString());
            entity.put("storage_service", "localFileStorageService");
            entity.put("file_size", 0);
            entity.put("md5", md5);
            entity.setState(EDBEntityState.Added);
            collNewFileInfo.getData().add(entity);
        }
        return dataAccess.update(collNewFileInfo);
    }

    public Integer checkFileIsExists() {
        DbCollection collection = getAllNewFile();
        collection.getData().parallelStream().forEach(entity -> {
            entity.setState(EDBEntityState.Modified);
            Long len = checkFile(entity);
            entity.put("is_exists", len > 0L);
            entity.put("file_size", len);
            entity.put("check_time", LocalDateTime.now());
        });
        return dataAccess.update(collection);
    }

    Long checkFile(DbEntity fileInfo) {
        FileStorageService storageService = getStorageService();
        return storageService.exists(fileInfo.get("file_path").toString(), fileInfo.get("file_name").toString());
    }


    DbCollection getAllOldFile() {
        QueryOption queryOption = new QueryOption("sd_upload_file");
        return dataAccess.queryAllData(queryOption);
    }

    public Integer MoveAllFileRelation(String preStr) {
        String[] preList = preStr.split(",");
        QueryOption queryOption = new QueryOption("dm_column");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.in("ele_type", "8,9");
        queryOption.setFixField("table_code,col_code");
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        for (DbEntity colInfo : collection.getData()) {
            boolean skip = false;
            for (String pre : preList) {
                if (colInfo.get("table_code").toString().startsWith(pre)) {
                    skip = true;
                    continue;
                }
            }
            if (skip) {
                continue;
            }
            MoveByTableAndColumn(colInfo.get("table_code").toString(), colInfo.get("col_code").toString());
        }
        return 1;
    }


    /**
     * 迁移文件关系
     *
     * @param tableCode
     * @param colCode
     */
    public void MoveByTableAndColumn(String tableCode, String colCode) {
        DbCollection collMove = getMoveInfo(tableCode, colCode);
        if (collMove.getData().size() > 0) {
            return;
        }
        DbCollection collBis = getBisData(tableCode, colCode);
        if (collBis.getData().size() == 0) {
            return;
        }
        Map<String, DbEntity> mapNewFileInfo = getAllNewFileInfo();

        DbCollection collRelation = dataAccess.getEmptyData("sd_file_relation");
        String dataColumnTag = String.format("%s,%s", collBis.getTableInfo().getId(), colCode);
        for (DbEntity bis : collBis.getData()) {
            String dataId = bis.getId().toString();
            String fileKey = TypeConverterUtils.object2String(bis.get(colCode), "");
            if (StringUtils.isEmpty(fileKey)) {
                continue;
            }
            DbCollection collOldFile = getOldFileData(dataId, fileKey);
            if (collOldFile.getData().size() == 0) {
                continue;
            }
            for (DbEntity oldEntity : collOldFile.getData()) {
                String md5 = oldEntity.get("md5").toString();
                if (!mapNewFileInfo.containsKey(md5)) {
                    continue;
                }
                DbEntity newFile = mapNewFileInfo.get(md5);

                DbEntity relation = collRelation.newEntity();
                relation.setState(EDBEntityState.Added);
                relation.put("app_id", "0");
                relation.put("data_id", dataId);
                relation.put("data_column_tag", dataColumnTag);
                relation.put("file_info_id", newFile.getId().toString());
                relation.put("original_name", oldEntity.get("file_name").toString());
                collRelation.getData().add(relation);
            }
        }
        //事务
        setMoveInfo(tableCode, colCode);
        dataAccess.update(collRelation);
    }

    Map<String, DbEntity> getAllNewFileInfo() {
        DbCollection collNewFileInfo = getAllNewFile();
        Map<String, DbEntity> mapMd5 = new HashMap<>();
        for (DbEntity entity : collNewFileInfo.getData()) {
            String md5 = entity.get("md5").toString();
            if (mapMd5.containsKey(md5)) {
                continue;
            }
            mapMd5.put(md5, entity);
        }
        return mapMd5;
    }

    DbCollection getAllNewFile() {
        QueryOption queryOption = new QueryOption("sd_file_info");
        DbCollection collNewFileInfo = dataAccess.queryAllData(queryOption);
        return collNewFileInfo;
    }

    DbCollection getBisData(String tableCode, String colCode) {
        QueryOption queryOption = new QueryOption(tableCode);
        queryOption.setFixField(colCode);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.isNotNull(colCode);
        queryOption.setTableFilter(filterWrapper.build());
        return dataAccess.queryAllData(queryOption);
    }

    DbCollection getOldFileData(String dataId, String fileKey) {
        QueryOption queryOption = new QueryOption("sd_upload_file");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("data_id", dataId).eq("file_key", fileKey);
        queryOption.setTableFilter(filterWrapper.build());
        return dataAccess.queryAllData(queryOption);
    }

    DbCollection getMoveInfo(String tableCode, String colCode) {
        QueryOption queryOption = new QueryOption("sd_move_info");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("table_code", tableCode).eq("col_code", colCode);
        queryOption.setTableFilter(filterWrapper.build());
        return dataAccess.queryAllData(queryOption);
    }

    void setMoveInfo(String tableCode, String colCode) {
        DbCollection coll = dataAccess.getEmptyData("sd_move_info");
        DbEntity entity = coll.newEntity();
        entity.put("table_code", tableCode);
        entity.put("col_code", colCode);
        entity.setState(EDBEntityState.Added);
        coll.getData().add(entity);
        dataAccess.update(coll);
    }
}
