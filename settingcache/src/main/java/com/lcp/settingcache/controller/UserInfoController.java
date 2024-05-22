package com.lcp.settingcache.controller;

import com.lcp.coremodel.DbEntity;
import com.lcp.settingcache.service.impl.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("userInfo")
@RestController
@Slf4j
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/getUserInfoByCode")
    public DbEntity getUserNameByCode(@RequestParam String userCode) {
        return userInfoService.getUserNameByCode(userCode);
    }

    @GetMapping("/getUserInfoById")
    public DbEntity getUserInfoById(@RequestParam Long id) {
        return userInfoService.getUserNameById(id);
    }

    @GetMapping("/getDeptInfoById")
    public DbEntity getDeptInfoByCode(@RequestParam Long id) {
        return userInfoService.getDeptInfoByCode(id);
    }
}
