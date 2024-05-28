package com.lubase.cache.service.impl;

import com.lubase.model.DbEntity;
import com.lubase.cache.constant.CacheConst;
import com.lubase.cache.mapper.CacheCoreTableMapper;
import com.lubase.cache.mapper.FileInfoMapper;
import com.lubase.cache.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {
    @Autowired
    CacheCoreTableMapper coreTableMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Cacheable(value = "uploadFile", key = CacheConst.PRE_CACHE_FILE_DATA + "+#key", unless = "#result == null")
    @Override
    public List<DbEntity> getFileDisplayNameByFileKey(String key) {
        if (StringUtils.isEmpty(key) || !key.contains("_")) {
            return null;
        }
        String dataId = key.split("_")[0];
        String fileKey = key.split("_")[1];
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(fileKey)) {
            return new ArrayList<>();
        }
        return coreTableMapper.getFileDisplayNameById(dataId, fileKey);
    }

    @Cacheable(value = "uploadFile", key = CacheConst.PRE_CACHE_FILE_DATA + "+#key", unless = "#result == null")
    @Override
    public List<DbEntity> getFileDisplayNameByFileKey2(String key) {
        if (StringUtils.isEmpty(key) || !key.contains("_")) {
            return null;
        }
        String dataId = key.split("_")[0];
        String fileKey = key.substring(key.indexOf("_") + 1);
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(fileKey)) {
            return new ArrayList<>();
        }
        return fileInfoMapper.getFileDisplayNameById(dataId, fileKey);
    }
}
