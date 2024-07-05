
package com.lubase.core.service.filemanage.service.impl;

import com.lubase.core.entity.SdFileInfoEntity;
import com.lubase.core.service.filemanage.dao.FileInfoDao;
import com.lubase.core.service.filemanage.dao.FileRelationDao;
import com.lubase.core.service.filemanage.model.FileInfoModel;
import com.lubase.core.service.filemanage.model.FileInfoVO;
import com.lubase.core.service.filemanage.service.FileManageService;
import com.lubase.core.service.filemanage.service.FileStorageService;
import com.lubase.model.DbEntity;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;

@Service
public class FileManageServiceImpl implements FileManageService, ExtendAppLoadCompleteService {

    @Value("${lubase.file-upload.storage-type:localFileStorageService}")
    private String storageType;
    Map<String, FileStorageService> fileStorageServices;
    @Autowired
    FileInfoDao fileInfoDao;
    @Autowired
    FileRelationDao relationDao;
    @Autowired
    CacheManager cacheManager;

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (fileStorageServices == null) {
            fileStorageServices = applicationContext.getBeansOfType(FileStorageService.class);
        }
    }

    @Override
    public void clearData() {
        fileStorageServices = null;
    }

    @SneakyThrows
    FileStorageService getStorageService(String storageService) {
        if (fileStorageServices.containsKey(storageService)) {
            return fileStorageServices.get(storageService);
        } else {
            throw new WarnCommonException("未找到FileStorageService服务，服务名字：" + storageService);
        }

    }

    @SneakyThrows
    @Override
    public String upload(FileInfoVO fileInfo) throws IOException {
        if (!fileInfo.getFileKey().contains(",")) {
            throw new WarnCommonException("fileKey参数格式不正确");
        }

        //1 文件md5检查
        String fileHash = generateHash(fileInfo.getData());
        DbEntity fileEntity = fileInfoDao.existSameFile(fileHash);
        if (fileEntity == null) {
            //2 文件写入
            String uniqueName = getUniqueName(fileInfo.getOriginalFileName());
            FileStorageService storageService = getStorageService(storageType);
            FileInfoModel infoModel = new FileInfoModel();
            String filePath = storageService.writeFile("", fileInfo.getOriginalFileName(), uniqueName, fileInfo.getData());
            infoModel.setGroupPath(filePath);
            infoModel.setFileSize(fileInfo.getData().length);
            infoModel.setOriginalFileName(fileInfo.getOriginalFileName());
            infoModel.setNewFileName(uniqueName);
            infoModel.setMd5(fileHash);
            infoModel.setStorageService(storageType);
            fileEntity = fileInfoDao.addFileInfo(infoModel);
        }
        //3 关系信息写入
        DbEntity relationEntity = relationDao.addFileRelation(fileInfo, fileEntity.getId().toString());
        clearCacheByFileRelation(relationEntity);
        return relationEntity.getId().toString();
    }

    @Override
    public SdFileInfoEntity getFileInfoByRelationId(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        DbEntity relationEntity = relationDao.getFileRelationById(id);
        if (relationEntity == null) {
            return null;
        }
        SdFileInfoEntity fileInfoEntity = fileInfoDao.getFileInfoById(relationEntity.get("file_info_id").toString());
        fileInfoEntity.setOriginal_name(relationEntity.get("original_name").toString());
        return fileInfoEntity;
    }

    @SneakyThrows
    @Override
    public Integer copyFileRelation(String fromDataId, String fromColumnTag, String toDataId, String toColumnTag) {
        if (StringUtils.isEmpty(fromDataId) || StringUtils.isEmpty(fromColumnTag) || StringUtils.isEmpty(toDataId) || StringUtils.isEmpty(toColumnTag)) {
            throw new WarnCommonException("参数不能为空");
        }
        return relationDao.copyFileRelation(fromDataId, fromColumnTag, toDataId, toColumnTag);
    }

    @Override
    public Integer copyFileRelation(String fromDataId, String fromColumnTag, String toDataId) {
        return copyFileRelation(fromDataId, fromColumnTag, toDataId, fromColumnTag);
    }

    @Override
    public byte[] readFile(SdFileInfoEntity fileInfo) {
        FileStorageService storageService = getStorageService(fileInfo.getStorage_service());
        return storageService.readFile(fileInfo.getFile_path(), fileInfo.getOriginal_name(), fileInfo.getFile_name());
    }

    @Override
    public Boolean deleteFileRelation(String appId, String dataId, String fileKey, String fileInfoId) {
        return relationDao.deleteFileRelation(appId, dataId, fileKey, fileInfoId);
    }

    @Override
    public Boolean deleteFileRelation(String id) {
        DbEntity entity = relationDao.getFileRelationById(id);
        if (entity != null) {
            Boolean result = false;
            clearCacheByFileRelation(entity);
            result = relationDao.deleteFileRelation(entity);
            clearCacheByFileRelation(entity);
            return result;
        } else {
            return false;
        }

    }

    private void clearCacheByFileRelation(DbEntity relationEntity) {
        Cache cache = cacheManager.getCache("uploadFile");
        if (cache != null) {
            String key = String.format("file:%s_%s", relationEntity.get("data_id").toString(), relationEntity.get("data_column_tag").toString());
            cache.evict(key);
        }
    }

    @Override
    public String getUniqueName(String originalFileName) {
        String newFileName = UUID.randomUUID().toString();
        if (originalFileName.contains(".")) {
            newFileName = newFileName + originalFileName.substring(originalFileName.indexOf("."));
        }
        return newFileName;
    }

    @Override
    public String generateHash(byte[] input) {
        try {
            //参数校验
            if (null == input) {
                return null;
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input);
            byte[] digest = md5.digest();
            BigInteger biNum = new BigInteger(1, digest);
            String hashText = biNum.toString(16);
            //32位
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (Exception e) {
            e.printStackTrace();
            //如果hash计算出错，则返回随机码，不影响正常保存
            return UUID.randomUUID().toString();
        }
    }

}
