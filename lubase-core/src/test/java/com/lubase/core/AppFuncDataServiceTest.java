package com.lubase.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.service.AppFuncDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AppFuncDataServiceTest {

    @Autowired
    AppFuncDataService appFuncDataService;

    @Test
    void testGetFuncInfoByFuncCode() {
        SsButtonEntity button = appFuncDataService.getFuncInfoByFuncCode("844352818671259648");
        assert button != null;
        System.out.println(JSON.toJSONString(button));
    }

    @Test
    void testGetFormIdByFuncCode() {
        String formId = appFuncDataService.getFormIdByFuncCode("835819990056701952");
        assert formId != null;
        System.out.println(formId);
        assert formId.equals("690428087560572928");
    }

    @Test
    public void testGetPageCanEditColumnWithNullPageEntity() {
        // Arrange
        SsPageEntity pageEntity = null;

        // Act
        String result = appFuncDataService.getPageCanEditColumn(pageEntity);

        // Assert
        assertNull(result, "The result should be null when pageEntity is null");
    }

    @Test
    public void testGetPageCanEditColumnWithEmptyGridInfo() {
        // Arrange
        SsPageEntity pageEntity = mock(SsPageEntity.class);
        when(pageEntity.getGrid_info()).thenReturn("");

        // Act
        String result = appFuncDataService.getPageCanEditColumn(pageEntity);

        // Assert
        assertNull(result, "The result should be null when grid_info is empty");
    }

    @Test
    public void testGetPageCanEditColumnWithInvalidGridInfo() {
        // Arrange
        SsPageEntity pageEntity = mock(SsPageEntity.class);
        when(pageEntity.getGrid_info()).thenReturn("invalid json");

        // Act
        String result = appFuncDataService.getPageCanEditColumn(pageEntity);

        // Assert
        assertNull(result, "The result should be null when grid_info is not a valid JSON");
    }

    @Test
    public void testGetPageCanEditColumnWithValidGridInfo() {
        // Arrange
        SsPageEntity pageEntity = mock(SsPageEntity.class);
        String gridInfo = "{\"editColumns\":\"col1,col2\"}";
        when(pageEntity.getGrid_info()).thenReturn(gridInfo);

        // Act
        String result = appFuncDataService.getPageCanEditColumn(pageEntity);

        // Assert
        Assert.hasText(result, "The result should not be empty when grid_info is valid");
        Assert.isTrue("col1,col2".equals(result), "The result should be 'col1,col2' when grid_info is valid");
    }

    @Test
    public void testGetPageRefTableWithNullPageEntity() {
        String refTable = appFuncDataService.getPageRefTable(null);
        assertNull(refTable, "The reference table should be null when the page entity is null.");
    }

    @Test
    public void testGetPageRefTableWithEmptyGridQuery() {
        SsPageEntity pageEntity = new SsPageEntity();
        pageEntity.setGrid_query("");
        String refTable = appFuncDataService.getPageRefTable(pageEntity);
        assertNull(refTable, "The reference table should be null when the grid query is empty.");
    }

    @Test
    public void testGetPageRefTableWithInvalidGridQuery() {
        SsPageEntity pageEntity = new SsPageEntity();
        pageEntity.setGrid_query("invalid_query");
        String refTable = appFuncDataService.getPageRefTable(pageEntity);
        assertNull(refTable, "The reference table should be null when the grid query is invalid.");
    }

    @Test
    public void testGetPageRefTableWithValidGridQuery() {
        SsPageEntity pageEntity = new SsPageEntity();
        QueryOption queryOption = new QueryOption();
        queryOption.setTableName("expected_table_name");
        pageEntity.setGrid_query(JSON.toJSONString(queryOption));

        String refTable = appFuncDataService.getPageRefTable(pageEntity);
        Assert.isTrue("expected_table_name".equals(refTable), "The reference table should match the expected table name.");
    }
}
