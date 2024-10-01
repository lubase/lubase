package com.lubase.core;

import com.lubase.core.extend.IGetLeftDataService;
import com.lubase.core.service.userright.model.ERoleType;
import com.lubase.orm.util.ServerMacroService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class BaseTest {

    @Autowired
    @Qualifier("core")
    private MessageSource messageSource;

    @Test
    void testLanguage() {

        String msg = messageSource.getMessage("core.lubase.hello", new Object[]{"world"}, LocaleContextHolder.getLocale());
        System.out.println(msg);
    }

    /**
     * 读取txt文件数据
     *
     * @param path
     * @return
     */
    protected String getStringFromFile(String path) {
        StringBuffer sb = new StringBuffer();
        try {
            Reader reader = new InputStreamReader(new FileInputStream(path));
            int ch = 0;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    @Test
    void EnumTest() {
        System.out.println(ERoleType.SupperAdministrator.ordinal());
        System.out.println(ERoleType.SupperAdministrator.getIndex());
        System.out.println(ERoleType.SupperAdministrator.toString().equals("1"));

        System.out.println(IGetLeftDataService.class.toString());
    }
}
