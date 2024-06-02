package com.lubase.core;

import com.lubase.core.model.CodeDataTypeVO;
import com.lubase.core.service.CodeDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CodeDataServiceTest {
    @Autowired
    CodeDataService codeDataService;

    @Test
    void TestGetCodeList() {
        List<CodeDataTypeVO> list = codeDataService.getCodeListForAppSetting("671085014334574592");
        System.out.println(list);

        list = codeDataService.getCodeListForAppSetting("1100454132705136640");
        System.out.println(list);
    }
}
