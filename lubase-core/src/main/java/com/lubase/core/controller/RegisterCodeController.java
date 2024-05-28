package com.lubase.core.controller;

import com.alibaba.fastjson.JSON;
import com.lubase.core.l0217.LicenseClientManagerService;
import com.lubase.core.l0217.LicenseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
public class RegisterCodeController {
    @Autowired
    LicenseClientManagerService licenseClientManagerService;

    @RequestMapping("/w")
    public String getRegisterInfo() {
        LicenseModel licenseModel = licenseClientManagerService.getLicense();
        if (licenseModel == null) {
            return "";
        }
        return JSON.toJSONString(licenseModel);
    }
}
