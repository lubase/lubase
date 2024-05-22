package com.lcp.qibao;

import com.lcp.core.util.SpringUtil;
import com.lcp.qibao.service.multiApplications.impl.JarURLClassLoader;
import lombok.SneakyThrows;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.LaunchedURLClassLoader;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class}, scanBasePackages = "com.lcp.*")
@MapperScan({"com.lcp.qibao.*.*.mapper"})
@EnableCaching
@EnableScheduling
public class QibaoApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(QibaoApplication.class, args);
        System.out.println("hello spring ……");
    }
}
