package com.lubase.core.runner;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.l0217.LicenseClientManagerService;
import com.lubase.core.l0217.LicenseModel;
import com.lubase.core.mapper.MultiDatabaseMapper;
import com.lubase.core.model.auto.DmDatabaseEntity;
import com.lubase.core.multiDataSource.DatabaseConnectBuilder;
import com.lubase.core.multiDataSource.DatabaseConnectModel;
import com.lubase.core.multiDataSource.DynamicDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Order(0)
@Component
@Slf4j
public class RegisterSlaveDBRunner implements ApplicationRunner {
    @Autowired
    MultiDatabaseMapper multiDatabaseMapper;
    @Autowired
    DatabaseConnectBuilder databaseConnectBuilder;
    @Autowired
    DynamicDataSource dynamicDataSource;

    @Autowired
    LicenseClientManagerService licenseClientManagerService;
    //   @Autowired
    // ToolSpring Tool;
//    @Autowired
//   List<InitCacheService> initCacheServiceList;
    /**
     * 应用ID，如果为空则表示为主应用
     */
    @Value("${appId:default}")
    private String appIdStr;

    @Value("${spring.datasource.druid-app.appointDatabase:}")
    private String appointDatabase;
    /***
     * licnese 是否跳过数据库数量的检查
     */
    @Value("${spring.datasource.druid-app.skipAppCountCheck:0}")
    private Integer skipAppCountCheck;
    /**
     * 应用ID
     */
    Long appId = 0L;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DmDatabaseEntity> coll;
        //无参数输入走加载所有
        if (appIdStr.equals("default")) {
            coll = multiDatabaseMapper.getAllDatabaseSetting();
            System.out.println("主应用模式启动……");
        } else {
            try {
                appId = Long.parseLong(appIdStr);
            } catch (Exception ex) {
                System.out.println("appId 输入错误:" + ex.getMessage());
            }
            //参数如果有问题,没有加载任何库
            coll = multiDatabaseMapper.getDatabaseSettingByAppId(appId);
            System.out.println("业务应用模式启动，应用ID:" + appId);
        }
        if (!checkLicense(multiDatabaseMapper.getAppCount())) {
            throw new InvokeCommonException("注册码校验失败，请联系管理员");
        }
        for (DmDatabaseEntity dmDatabaseEntity : coll) {
            // 因为默认数据源就是主数据源，所以不用再次注册
            if (dmDatabaseEntity.getId() == 0L) {
                continue;
            }
            if (!StringUtils.isEmpty(appointDatabase) && !appointDatabase.equals(dmDatabaseEntity.getDatabase_name())) {
                continue;
            }
            DatabaseConnectModel database = databaseConnectBuilder.buildConnectModel(dmDatabaseEntity);
            Boolean result = dynamicDataSource.createDataSourceWithCheck(database);
            System.out.println(String.format("注册数据源：%s,注册结果：%s", database.getAliasCode(), result));
        }
        System.out.println("数据源创建完成…………");
    }

    @SneakyThrows
    private Boolean checkLicense(Integer appCount) {
        //去掉默认的后端管理应用
        appCount = appCount - 1;
        String registerCode = licenseClientManagerService.getRegisterCodeFromClient();
        String applyCode = "";
        applyCode = licenseClientManagerService.getUniqueCode2();
        if (registerCode.length() == 0) {
            log.error("未找到注册码，您的申请码是：" + applyCode);
        } else {
            try {
                licenseClientManagerService.checkRegisterCode2(registerCode);
                LicenseModel licenseModel = licenseClientManagerService.getLicense();
                if (licenseModel == null || StringUtils.isEmpty(licenseModel.getMode())) {
                    log.error("注册码校验失败，请联系管理员。您的申请码是：" + applyCode);
                    return false;
                } else if (licenseModel.getMode().equals("2")) {
                    if (appCount > licenseModel.getAppCount() && skipAppCountCheck.equals(0)) {
                        log.error(String.format("启动失败：应用数量为%s，超出了所授权的应用数量%s", appCount, licenseModel.getAppCount()));
                        return false;
                    }
                }
                return true;
            } catch (Exception ex) {
                log.error("校验注册码失败，您的申请码是：" + applyCode);
            }
        }
        return false;
    }
}
