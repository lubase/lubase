package com.lubase.core.service.multiApplications.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExtendFileModel {
    private String groupId;
    private String filePath;
    private String fileName;
    private String lastModifiedTime;
    /**
     * 加载状态
     */
    private Boolean isLoaded = false;

    public void setIsLoaded(Boolean loaded) {
        this.isLoaded = loaded;
        lastLoadTime = LocalDateTime.now();
    }

    private String extendFileId;
    /**
     * 最后加载时间
     */
    private LocalDateTime lastLoadTime;
}
