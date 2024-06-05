package com.lubase.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.core.model.PageInfoVO;
import com.lubase.core.model.SearchCondition;
import com.lubase.core.service.RenderPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RenderPageServiceTest {

    @Autowired
    RenderPageService renderPageService;
    @Autowired
    DataAccess dataAccess;

    @Autowired
    AppHolderService appHolderService;

    @Test
    void test() {
        String searchStr = "[{\"columnCode\":\"table_code\",\"filterType\":2,\"orderId\":2,\"required\":0,\"aliasName\":\"\",\"disType\":\"\",\"defaultValue\":\"\",\"defaultValueName\":\"\"},{\"columnCode\":\"col_code,col_name\",\"filterType\":1,\"orderId\":2,\"required\":0,\"aliasName\":\"列代码或名称\",\"disType\":\"\",\"defaultValue\":\"\",\"defaultValueName\":\"\"}]";
        String tableCode = "dm_column";
        List<SearchCondition> list = JSON.parseArray(searchStr, SearchCondition.class);

        System.out.println(JSON.toJSONString(list));

        QueryOption queryOption = new QueryOption(tableCode);
        String fieldStr = "";
        for (SearchCondition condition : list) {
            fieldStr += "," + condition.getColumnCode();
        }
        System.out.println(fieldStr.substring(1));
        queryOption.setFixField(fieldStr.substring(1));
        DbCollection coll = dataAccess.queryFieldList(queryOption);

        //System.out.println(JSON.toJSONString(coll.getTableInfo().getFieldList()));
    }

    @Test
    void testGetPageInfo() {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(688163728524316672L);
        appHolderService.setUser(loginUser);

        PageInfoVO pageInfoVO = renderPageService.getPageInfo("2022052921131388348");
        System.out.println(JSON.toJSONString(pageInfoVO.getSearch()));
    }
}