package com.lcp.qibao;

import com.lubase.core.extend.IGetLeftDataService;
import com.lubase.core.service.userright.model.ERoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@SpringBootTest
@Slf4j
public class BaseTest {
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
