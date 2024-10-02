package com.lubase.cache.controller;

import com.lubase.model.*;
import com.lubase.cache.service.RegisterColumnInfoService;
import com.lubase.cache.service.UpdateSettingDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/registerColumnInfo")
@RestController
@Slf4j
public class RegisterColumnInfoController {

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    UpdateSettingDataService updateSettingDataService;

    @GetMapping("/initTableInfoByTableId")
    public DbTable initTableInfoByTableId(@RequestParam Long tableId) {
        DbTable table = registerColumnInfoService.initTableInfoByTableId(tableId);
        return table;
    }

    @GetMapping("/initTableInfoByTableCode")
    public DbTable initTableInfoByTableCode(@RequestParam String tableCode) {
        log.info(tableCode);
        DbTable table = registerColumnInfoService.initTableInfoByTableCode(tableCode);
        return table;
    }

    @GetMapping("/getTableIdByTableCode")
    public String getTableIdByTableCode(@RequestParam String tableCode) {
        log.info(tableCode);
        return registerColumnInfoService.getTableIdByTableCode(tableCode);
    }

    @GetMapping("/getTableCodeByTableId")
    public String getTableCodeByTableId(@RequestParam Long tableId) {
        return registerColumnInfoService.getTableCodeByTableId(tableId);
    }

    @GetMapping("/getColumnInfoByColumnId")
    public DbField getColumnInfoByColumnId(@RequestParam Long columnId) {
        return registerColumnInfoService.getColumnInfoByColumnId(columnId);
    }

    @GetMapping("/getColumnsByTableId")
    public List<DbField> getColumnsByTableId(@RequestParam Long tableId) {
        return registerColumnInfoService.getColumnsByTableId(tableId);
    }

    @GetMapping("/getTableCacheSettingList")
    public List<SsCacheEntity> getTableCacheSettingList() {
        return updateSettingDataService.getTableCacheSettingList();
    }

    @GetMapping("/getTableRelateSettingList")
    public List<DbEntity> getTableRelateSettingList() {
        return updateSettingDataService.getTableRelateSettingList();
    }

    @GetMapping("/getControlledTableList")
    public List<String> getControlledTableList() {
        return registerColumnInfoService.getControlledTableList();
    }

    @GetMapping("/getCodeListByTypeId")
    public List<DbCode> getControlledTableList(String codeTypeId) {
        return registerColumnInfoService.getCodeListByTypeId(codeTypeId);
    }

    @GetMapping("/getResourceByAppId")
    public List<ResourceDataModel> getResourceList(String appId, String tableCode) {
        return registerColumnInfoService.getResourceList(appId,tableCode);
    }
}
