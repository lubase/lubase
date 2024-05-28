package com.lcp.qibao;

import com.lubase.starter.service.PersonalizationDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonalizationDataServiceTest {

    @Autowired
    PersonalizationDataService personalizationDataService;

    @Test
    void testDisplayColumnSetting() {
        long pageId, accountId;
        pageId = 1007L;
        accountId = 1008L;
        String columnIds = "id1,id2,id3";
        int lockColumnCount = 2;
        personalizationDataService.saveDisplayColumn(pageId, accountId, columnIds, lockColumnCount);

        String columnIdsFromDb = personalizationDataService.getDisplaySetting(pageId, accountId).getColumnIds();
        System.out.println(columnIdsFromDb);

        assert columnIds.equals(columnIdsFromDb);
    }

    @Test
    void testUpdateDisplaySetting() {
        long pageId, accountId;
        pageId = 1007L;
        accountId = 1008L;
        String columnIds = "id2,id3";
        int lockColumnCount = 2;

        personalizationDataService.saveDisplayColumn(pageId, accountId, columnIds, lockColumnCount);

        String columnIdsFromDb = personalizationDataService.getDisplaySetting(pageId, accountId).getColumnIds();
        System.out.println(columnIdsFromDb);
        assert columnIds.equals(columnIdsFromDb);

        columnIds = "id1,id2,id3,id4";
        personalizationDataService.saveDisplayColumn(pageId, accountId, columnIds, lockColumnCount);

        for (int i = 0; i < 10; i++) {
            columnIdsFromDb = personalizationDataService.getDisplaySetting(pageId, accountId).getColumnIds();
            System.out.println(columnIdsFromDb);
            assert columnIds.equals(columnIdsFromDb);
        }
    }

    @Test
    void testUpdateColumnWidthSetting() {
        long pageId, accountId;
        pageId = 1007L;
        accountId = 1008L;
        String columnWidthSetting = "{\\\"col1\\\":\\\"120\\\",\\\"col2\\\":\\\"200\\\"}";

        personalizationDataService.saveColumnWidthSetting(pageId, accountId, columnWidthSetting);
    }
}
