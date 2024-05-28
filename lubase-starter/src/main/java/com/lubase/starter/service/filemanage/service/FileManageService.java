package com.lubase.starter.service.filemanage.service;


import com.lubase.starter.auto.entity.SdFileInfoEntity;
import com.lubase.starter.service.filemanage.model.FileInfoVO;

import java.io.IOException;

public interface FileManageService {
    /**
     * 上传文件
     *
     * @param fileReqParamBO
     * @return
     * @throws IOException
     */
    String upload(FileInfoVO fileReqParamBO) throws IOException;

    /**
     * 读取文件流
     *
     * @param fileInfo
     * @return
     */
    byte[] readFile(SdFileInfoEntity fileInfo);

    /**
     * 存储文件前，重命名为不重复名字
     *
     * @param originalFileName
     * @return
     */
    String getUniqueName(String originalFileName);

    /**
     * 获取文件hash值
     *
     * @param input
     * @return
     */
    String generateHash(byte[] input);

    SdFileInfoEntity getFileInfoByRelationId(String id);

    /**
     * 赋值文件
     *
     * @param fromDataId
     * @param fromColumnTag
     * @param toDataId
     * @param toColumnTag
     * @return
     */
    Integer copyFileRelation(String fromDataId, String fromColumnTag, String toDataId, String toColumnTag);

    /**
     * 赋值文件
     *
     * @param fromDataId
     * @param fromColumnTag
     * @param toDataId
     * @return
     */
    Integer copyFileRelation(String fromDataId, String fromColumnTag, String toDataId);

    Boolean deleteFileRelation(String appId, String dataId, String fileKey, String fileInfoId);

    Boolean deleteFileRelation(String id);
}
