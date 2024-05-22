package com.lcp.settingcache.controller;

import com.lcp.coremodel.DbEntity;
import com.lcp.settingcache.service.FileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("fileInfo")
@RestController
@Slf4j
public class FileInfoController {

    @Autowired
    FileInfoService fileInfoService;

    @GetMapping("/getFileDisplayNameByFileKey")
    public List<DbEntity> getFileDisplayNameByFileKey(String dataId, String fileKey) {
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(fileKey)) {
            return new ArrayList<>();
        }
        String key = String.format("%s_%s", dataId, fileKey);
        return fileInfoService.getFileDisplayNameByFileKey(key);
    }

    @GetMapping("/getFileDisplayNameByFileKey2")
    public List<DbEntity> getFileDisplayNameByFileKey2(String dataId, String fileKey) {
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(fileKey)) {
            return new ArrayList<>();
        }
        String key = String.format("%s_%s", dataId, fileKey);
        return fileInfoService.getFileDisplayNameByFileKey2(key);
    }
}
