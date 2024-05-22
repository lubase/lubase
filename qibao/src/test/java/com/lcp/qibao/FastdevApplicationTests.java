package com.lcp.qibao;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FastdevApplicationTests {

    @Test
    void contextLoads() {
        FastAutoGenerator.create("jdbc:sqlserver://47.108.130.151:22345;Databasename=fastdev;instanceName=SQLEXPRESS", "sa", "1234.com.cn.a")
                .globalConfig(builder -> {
                    builder.author("A") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(".\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.lcp.fastdev.auto") // 设置父包名
                            .moduleName("generator"); // 设置父包模块名
                    //.pathInfo(Collections.singletonMap(OutputFile.mapperXml, ".\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("SSPAGE") // 设置需要生成的表名
                            .addTablePrefix("su", ""); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
