package com.lubase.core.tool;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MybatisTool {
    @Test
    void contextLoads() {
        FastAutoGenerator.create("jdbc:sqlserver://192.168.0.168;Databasename=fastdev", "sa", "1234.com")
                .globalConfig(builder -> {
                    builder.author("A") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(".\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.lcp.fastdev.auto") // 设置父包名
                            .moduleName("generator");// 设置父包模块名
                    //.pathInfo(Collections.singletonMap(OutputFile.mapperXml, ".\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("", "sscolumn") // 设置需要生成的表名
                            .addTablePrefix("su", ""); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
