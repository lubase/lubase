package com.lcp.qibao;

import com.lcp.qibao.service.RenderTableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FullTextServiceTest {

    @Autowired
    RenderTableService renderTableService;

    @Test
    void testSearch() {
        String str = "";
        String pageId = "";
        String result = renderTableService.getIdListByFullTextSearch(pageId, str);
        assert result.equals("0");
        str = "4";
        result = renderTableService.getIdListByFullTextSearch(pageId, str);
        System.out.println(result);
        assert result.equals("1,2,3,4");
    }
}
