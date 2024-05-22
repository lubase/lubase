package com.lcp.qibao.tool;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.lcp.coremodel.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateEntity {

    @Test
    void createOneEntity() {
//        createOneEntity("dm_code", "DmCodeEntity");
//        createOneEntity("dm_code_type", "DmCodeTypeEntity");
//        createOneEntity("dm_column", "DmColumnEntity");
//        createOneEntity("dm_custom_form", "DmCustomFormEntity");
//        createOneEntity("dm_database", "DmDatabaseEntity");
//        createOneEntity("dm_table", "DmTableEntity");
//        createOneEntity("dm_table_type", "DmTableTypeEntity");
//        createOneEntity("sa_account", "SaAccountEntity");
//        createOneEntity("sd_upload_file", "SdUploadFileEntity");
//        createOneEntity("ss_button", "SsButtonEntity");
//        createOneEntity("ss_extend_file", "SsExtendFileEntity");
        //createOneEntity("ss_form_trigger", "SsFormTriggerEntity2");
//        createOneEntity("ss_invoke_datasource", "SsInvokeDatasourceEntity");
//        createOneEntity("ss_invoke_method", "SsInvokeMethodEntity");
       // createOneEntity("sd_file_info", "SdFileInfoEntity");
       // createOneEntity("sd_file_relation", "SdFileRelation2");

//        createOneEntity("ss_table_trigger", "SsTableTriggerEntity");
//        createOneEntity("dm_code", "DmCodeEntity");
//        createOneEntity("dm_code", "DmCodeEntity");
//        createOneEntity("dm_col_template", "DmColTemplateEntity");
        //createOneEntity("dm_custom_form", "DmCustomFormEntity2");
        createOneEntity("ss_page", "ss_page2");
        //createOneEntity("ss_button", "ss_button2");
    }

    void createOneEntity(String tableCode, String entityName) {
        FastAutoGenerator.create("jdbc:mysql://101.43.198.190:5677/lcp?allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai", "root", "1234.com")
                .globalConfig(builder -> {
                    builder.author("A") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() //不打开文件夹
                            .outputDir(".\\src\\main\\java"); // 指定输出目录
                }).templateConfig(builder -> {
                    builder.disable(TemplateType.CONTROLLER)
                            .disable(TemplateType.SERVICEIMPL)
                            .disable(TemplateType.SERVICE)
                            .disable(TemplateType.XML)
                            .disable(TemplateType.MAPPER)
                            .disable(TemplateType.ENTITY)
                            .entity("/templates/entity.java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.lcp.qibao") // 设置父包名
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
