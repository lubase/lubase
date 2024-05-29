package com.lubase.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * @author A
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class}, scanBasePackages = "com.lubase.*")
@MapperScan({"com.lubase.core.*.*.mapper"})
@EnableCaching
@EnableScheduling
public class QibaoApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(QibaoApplication.class, args);
        System.out.println("hello spring ……");
    }
}
