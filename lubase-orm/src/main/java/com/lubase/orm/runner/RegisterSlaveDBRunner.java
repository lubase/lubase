package com.lubase.orm.runner;

import com.lubase.orm.mapper.MultiDatabaseMapper;
import com.lubase.orm.model.auto.DmDatabaseEntity;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.multiDataSource.DatabaseConnectBuilder;
import com.lubase.orm.multiDataSource.DatabaseConnectModel;
import com.lubase.orm.multiDataSource.DynamicDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    /**
     * 应用ID，如果为空则表示为主应用
     */
    @Value("${appId:default}")
    private String appIdStr;

    @Value("${lubase.appoint-database:}")
    private String appointDatabase;
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
        List<String> appointDatabaseList = StringUtils.isEmpty(appointDatabase) ? new ArrayList<>() : List.of(appointDatabase.split(","));

        coll.parallelStream().forEach(dmDatabaseEntity -> {
            // 因为默认数据源就是主数据源，所以不用再次注册
            if (dmDatabaseEntity.getId() == 0L) {
                return;
            }
            if (!StringUtils.isEmpty(appointDatabase) && !appointDatabaseList.contains(dmDatabaseEntity.getDatabase_name())) {
                return;
            }
            DatabaseConnectModel database = databaseConnectBuilder.buildConnectModel(dmDatabaseEntity);
            Boolean result = null;
            try {
                result = dynamicDataSource.createDataSourceWithCheck(database);
                System.out.println(String.format("注册数据源：%s,注册结果：%s", database.getAliasCode(), result));
                DBContextHolder.setDataSourceCode(dmDatabaseEntity.getId().toString());
                multiDatabaseMapper.initMonster(dmDatabaseEntity.getDatabase_name());

            } catch (Exception e) {
                log.error("注册数据源失败" + database.getAliasCode(), e);
            }
        });

        System.out.println("数据源创建完成…………");
    }
}
