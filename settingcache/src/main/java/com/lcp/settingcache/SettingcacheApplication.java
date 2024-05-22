package com.lcp.settingcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.lcp.*"})
@EnableCaching
@MapperScan("com.lcp.settingcache.mapper")
public class SettingcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettingcacheApplication.class, args);
    }

}
