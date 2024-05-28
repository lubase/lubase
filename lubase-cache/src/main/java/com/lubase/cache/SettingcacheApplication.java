package com.lubase.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.lubase.*"})
@EnableCaching
@MapperScan("com.lubase.cache.mapper")
public class SettingcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettingcacheApplication.class, args);
    }

}
