package com.lcp.qibao.service.filemanage.model;

import lombok.Data;

@Data
public class FileInfoVO {
    /**
     * 关联字段id
     */
    private String fileKey;
    /**
     * 数据id
     */
    private String dataId;

    /**
     * 文件数据
     */
    private byte[] data;

    /**
     * 文件原始名称
     */
    private String originalFileName;

    private Long appId;
}
