package com.lcp.qibao;

import com.lubase.starter.service.filemanage.service.MoveOldFIleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MoveOldFIleServiceTest {

    @Autowired
    MoveOldFIleService moveOldFIleService;

    @Test
    void testMoveFileToFileInfo() {
        Integer result = moveOldFIleService.moveFileToFileInfo();
        System.out.println("迁移文件数量：" + result);
    }

    @Test
    void testMoveByTableAndColumn() {
        String tableCode = "form_test";
        String colCode = "colfile80";
        moveOldFIleService.MoveByTableAndColumn(tableCode, colCode);
    }

    @Test
    void testAllRelation() {
        moveOldFIleService.MoveAllFileRelation("v_,bj_");
    }

    @Test
    void testExists() {
        moveOldFIleService.checkFileIsExists();
    }
}
