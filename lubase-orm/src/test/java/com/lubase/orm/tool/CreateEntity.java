package com.lubase.orm.tool;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CreateEntity {

    @Test
    void createOneEntity() {
        createOneEntity("dm_database", "DmDatabaseEntity2");
    }

    @Value("${spring.datasource.druid.url}")
    private String url;

    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    void createOneEntity(String tableCode, String entityName) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("A") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() //不打开文件夹
                            .outputDir(".\\src\\main\\java"); // 指定输出目录
                }).templateConfig(builder -> {
                    builder.disable(TemplateType.CONTROLLER)
                            .disable(TemplateType.SERVICEIMPL)
                            .disable(TemplateType.SERVICE)
                            .disable(TemplateType.MAPPER)
                            .disable(TemplateType.XML)
                            .disable(TemplateType.ENTITY)
                            .entity("/templates/entity.java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.lcp.core") // 设置父包名
                            .moduleName("auto"); // 设置父包模块名
                    //.pathInfo(Collections.singletonMap(OutputFile.mapperXml, ".\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            .naming(NamingStrategy.no_change)
                            .columnNaming(NamingStrategy.no_change)
                            .formatFileName(entityName)
                            .superClass(DbEntity.class);
                    builder.addInclude(tableCode); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
