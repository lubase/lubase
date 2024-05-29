package com.lubase.core.service.multiApplications.model;

import lombok.Data;

/**
 * <p>
 *   文件上传，请求参数
 * </p>
 * @author zhulz
 * @jdk   1.8
 */
@Data
public class FileReqParamBO {

    private  Long id;
    /**
     * 文件Md5值
     */
    private String md5;
    /**
     * 关联字段id
     */
    private String fileKey;
    /**
     * 数据id
     */
    private Long dataId;


    /**
     * 文件数据
     */
    private byte[] data;

    /**
     * 文件大小 k
     */
    private  double size;

    /**
     * 文件原始名称
     */
    private  String fileName;

    private  String localPath;
}
