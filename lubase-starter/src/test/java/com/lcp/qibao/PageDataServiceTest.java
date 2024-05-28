package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.model.DbCollection;
import com.lubase.core.operate.EOperateMode;
import com.lubase.core.TableFilter;
import com.lubase.core.service.AppHolderService;
import com.lubase.model.DbEntity;
import com.lubase.starter.model.SearchVO;
import com.lubase.starter.service.RenderPageService;
import com.lubase.starter.service.RenderTableService;
import com.lubase.starter.service.RenderTreeService;
import com.lubase.starter.util.ClientMacro;
import com.lubase.starter.service.PageDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PageDataServiceTest {

    @Autowired
    RenderPageService renderPageService;
    @Autowired
    PageDataService pageDataService;

    @Autowired
    RenderTableService renderTableService;

    @Autowired
    RenderTreeService renderTreeService;

    @Autowired
    AppHolderService appHolderService;
    @Test
    void getGridData() {

    }

    @Test
    void testGetStatisticsInfoList() {
        String pageId = "746696222764437504"; // 040212 分组报表测试
        String clientParams = "{}";
        ClientMacro clientMacro = ClientMacro.init("");
        DbCollection coll = renderTableService.getStatisticsInfo(pageId, clientParams, clientMacro, "5", "2");
        System.out.println("没有条件时记录数：" + coll.getTotalCount());
        System.out.println("数据明细：：" + coll);
    }



    @Test
    void getTreeData() {
        String pageCode = "2022052921061269279";
        List<DbEntity> coll = renderTreeService.getTreeDataByPageId(pageCode, ClientMacro.init(""));
        System.out.println("--------------" + pageCode);
        System.out.println(coll);
        //test  feature branch
    }

    @Test
    void getTreeData010101() {
        String pageCode = "010101";
        String clientParams = "";
        List<DbEntity> coll = renderTreeService.getTreeDataByPageId(pageCode, ClientMacro.init(""));
        System.out.println("--------------" + pageCode);
        System.out.println("count is " + coll.size());
        //test  feature branch
    }

    @Test
    void GetParms(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setContentType(type);

        JSONObject object = new JSONObject();
        object.put("pageCode", "010109");
        object.put("clientParams", "123");
        HttpEntity<String> formEntity = new HttpEntity<>("pageCode=010109&clientParams=22", headers);

        String resultStr = restTemplate.postForObject("/page/test2", formEntity, String.class);
        System.out.println("resultStr is " + resultStr);
        assert object.toJSONString().equals(resultStr);
    }

    @Test
    void objectTest() {
        JSONObject object = new JSONObject();
        object.put("pageCode", "010109");
        object.put("clientParams", "clientParams");
        System.out.println(object.toString());
    }

    @Test
    void testGetSearchInfo() {
        String mainTableCode = "sa_account";
        TableFilter searchFilter = new TableFilter();
        searchFilter.setChildFilters(new ArrayList<>());

        TableFilter f11 = new TableFilter();
        f11.setChildFilters(new ArrayList<>());
        f11.getChildFilters().add(new TableFilter("user_code", ""));
        f11.getChildFilters().add(new TableFilter("user_name", ""));
        searchFilter.getChildFilters().add(f11);

        Object result = ReflectionTestUtils.invokeMethod(pageDataService, "getSearchVO", mainTableCode, searchFilter);
        assert result instanceof SearchVO;
        SearchVO searchVO = (SearchVO) result;
        assert searchVO.getFieldInfo() != null;
        System.out.println(searchVO.getFieldInfo());
        assert searchVO.getFieldInfo().size() == 2;
    }

    @Test
    void testGetSearchInfoRange() {
        String mainTableCode = "sa_account";
        TableFilter searchFilter = new TableFilter();
        searchFilter.setChildFilters(new ArrayList<>());

        TableFilter f11 = new TableFilter();
        f11.setChildFilters(new ArrayList<>());
        f11.getChildFilters().add(new TableFilter("user_code", ""));
        TableFilter f11Range = new TableFilter();
        f11Range.setChildFilters(new ArrayList<>());
        f11Range.getChildFilters().add(new TableFilter("phone", "", EOperateMode.GreateEquals));
        f11Range.getChildFilters().add(new TableFilter("phone", "", EOperateMode.LessEquals));
        f11.getChildFilters().add(f11Range);
        searchFilter.getChildFilters().add(f11);

        Object result = ReflectionTestUtils.invokeMethod(pageDataService, "getSearchVO", mainTableCode, searchFilter);
        assert result instanceof SearchVO;
        SearchVO searchVO = (SearchVO) result;
        assert searchVO.getFieldInfo() != null;
        System.out.println(searchVO.getFieldInfo());
        for (String key : searchVO.getFieldInfo().keySet()) {
            System.out.println(JSON.toJSONString(searchVO.getFieldInfo().get(key)));
        }
        assert searchVO.getFieldInfo().size() == 2;
    }

    @Test
    void testGetSearchInfoContainParentTable() {
        String mainTableCode = "ss_button";
        TableFilter searchFilter = new TableFilter();
        searchFilter.setChildFilters(new ArrayList<>());

        TableFilter f11 = new TableFilter();
        f11.setChildFilters(new ArrayList<>());
        f11.getChildFilters().add(new TableFilter("button_name", ""));
        f11.getChildFilters().add(new TableFilter("page_id.page_name", ""));
        searchFilter.getChildFilters().add(f11);

        Object result = ReflectionTestUtils.invokeMethod(pageDataService, "getSearchVO", mainTableCode, searchFilter);
        assert result instanceof SearchVO;
        SearchVO searchVO = (SearchVO) result;
        assert searchVO.getFieldInfo() != null;
        System.out.println(searchVO.getFieldInfo());
        for (String key : searchVO.getFieldInfo().keySet()) {
            System.out.println(JSON.toJSONString(searchVO.getFieldInfo().get(key)));
        }
        assert searchVO.getFieldInfo().size() == 2;
    }
}
