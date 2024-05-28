package com.lubase.cache.service;

import com.lubase.model.DbEntity;

import java.util.List;

public interface FileInfoService {

    /**
     * 获取附件显示名称
     *
     * @param fileKey
     * @return
     */
    List<DbEntity> getFileDisplayNameByFileKey(String key);
    List<DbEntity> getFileDisplayNameByFileKey2(String key);
}
