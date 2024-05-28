package com.lubase.starter.service.filemanage.model;

import lombok.Data;

@Data
public class FileInfoModel {
    private String storageService;
    private String groupPath;
    private String originalFileName;
    private String newFileName;
    private String md5;
    private Integer fileSize;
}
