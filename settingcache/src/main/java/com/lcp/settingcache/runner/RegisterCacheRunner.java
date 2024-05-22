package com.lcp.settingcache.runner;

import com.lcp.settingcache.service.InitCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(0)
@Component
@Slf4j
public class RegisterCacheRunner implements ApplicationRunner {

    @Autowired
    List<InitCacheService> initCacheServiceList;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("缓存初始化开始");
        for (InitCacheService cacheService : initCacheServiceList) {
            //cacheService.appStartInitCache();
            System.out.println(String.format("%s 缓存初始化完成", cacheService.getClass().getName()));
        }
        System.out.println("缓存初始化完成…………");

    }
}
