package com.lcp.qibao.multiDataSource;

import com.alibaba.fastjson.JSONObject;
import com.lcp.qibao.BaseTest;
import com.lubase.core.service.DataAccess;
import com.lubase.core.service.IDGenerator;
import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.TableFilter;
import com.lubase.core.multiDataSource.DynamicDataSource;
import com.lubase.core.service.RegisterColumnInfoService;
import com.lubase.model.DbEntity;
import com.lubase.starter.controller.InvokeController;
import com.lubase.starter.model.InvokeMethodParamDTO;
import com.lubase.starter.service.impl.RenderFormServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;


@SpringBootTest
public class MultiDataSourceTest extends BaseTest {

    @Autowired
    DataAccess dataAccess;
    @Autowired
    RegisterColumnInfoService sstableService;

   //@Autowired
   // ISsdatabasesourceService databasesourceService;

    @Autowired
     IDGenerator idGenerator;

    @Autowired
    RenderFormServiceImpl formDataService;
    @Autowired
    DynamicDataSource dynamicDataSource;

    @Autowired
    InvokeController invokeController;


    //创建库

    @Test
    void createDatabaseTest() throws Exception {
//        DbCollection dbCollection = formDataService.getEditDateById("SSDATABASESOURCE", "22222222222222222223");
//        dbCollection.getData().get(0).put("CODE", "mysql8_test03");
//        dbCollection.getData().get(0).put("CODE", "mysql8_test03");
//        dbCollection.getData().get(0).put("ID", "");
//        dbCollection.getData().get(0).put("STATE", 1);
        //dbSetService.update(dbCollection);
    }

    //创建表
    @Test
    void createTableTest() throws Exception {

//        DmTableEntity sstable = sstableMapper.getTableInfo("SSTABLE");
//        sstable.setDatabase_id(1111111111111111111L);
//        sstable.setTable_code("SSTABLE_TEST0427");
//        sstable.setTable_name("注册表_Test0427");
//        DBTableBO dbTableBO = new DBTableBO();
//        BeanUtils.copyProperties(sstable, dbTableBO);
//        dbTableBO.setState(1);
//        dbTableBO.setId(idGenerator.nextId());
//        //  dbTableBO.setId("00002NBX49NV40000A01");
//        String s = JSON.toJSONString(dbTableBO);
//
//        InvokeMethodParamDTO imp = new InvokeMethodParamDTO();
//        imp.setFuncCode("010206_btnSaveTable");
//        HashMap<String, String> map = new HashMap<>();
//        map.put("TDATA", s);
//        imp.setData(map);
//        invokeController.invokeOneData(imp);
    }

    //创建字段
    @Test
    void createField() throws Exception {

//        DmColumnEntity sscolumn = sscolumnService.getById(1L);
//        DbField dbFieldBO = new DbField();
//        BeanUtils.copyProperties(sscolumn, dbFieldBO);
//        dbFieldBO.setId(DataKeyFactory.instance.createCurrentKey());
//        dbFieldBO.setCode("Test001");
//        dbFieldBO.setName("测试0001");
//        String s = JSON.toJSONString(dbFieldBO);
//        InvokeMethodParamDTO imp = new InvokeMethodParamDTO();
//        imp.setFuncCode("010206_btnSaveColumn");
//        HashMap<String, String> map = new HashMap<>();
//        map.put("CDATA", s);
//        imp.setData(map);
//        invokeController.invokeOneData(imp);
    }

    @Test
    void testConnect() throws Exception {

//        DbCollection dbCollection = formDataService.getEditDateById("SSDATABASESOURCE", "22222222222222222223");
//        DBSourceBO ssdatabasesource = CommonUtil.mapToObject(dbCollection.getData().get(0), DBSourceBO.class);
//        ssdatabasesource = CommonUtil.getDBUrl(ssdatabasesource, true);
//        boolean b = dynamicDataSource.testDatasource(ssdatabasesource.getDriveclass(), ssdatabasesource.getUrl(), ssdatabasesource.getName(), ssdatabasesource.getPassword());
//        assert b;
//
//        dbCollection = formDataService.getEditDateById("SSDATABASESOURCE", "11111111111111111111");
//        ssdatabasesource = CommonUtil.mapToObject(dbCollection.getData().get(0), DBSourceBO.class);
//        ssdatabasesource = CommonUtil.getDBUrl(ssdatabasesource, true);
//        b = dynamicDataSource.testDatasource(ssdatabasesource.getDriveclass(), ssdatabasesource.getUrl(), ssdatabasesource.getName(), ssdatabasesource.getPassword());
        //assert b;
    }

    @Test
    void testConnect2() throws Exception {

//        DbCollection dbCollection = formDataService.getEditDateById("SSDATABASESOURCE", "11111111111111111111");
//        HashMap<String, String> mpData = new HashMap<String,String>();
//        for (String key : dbCollection.getData().get(0).keySet()) {
//            mpData.put(key, String.valueOf(dbCollection.getData().get(0).get(key)));
//        }
//       // boolean b = dbSetService.testConnect(mpData);
//        //assert b;

    }

    @Test
    void getCustomFormDataTest() {

        InvokeMethodParamDTO dto = new InvokeMethodParamDTO();
        //dto.setMethodId("00001PZ67YJLI0000A06");
        dto.setMethodId(1L);
        dto.setFuncCode("010519");
        HashMap<String, String> map = new HashMap<>();
        map.put("p1", "SSCODE");
        dto.setData(map);
        Object datasource = invokeController.datasource(dto);
        String s1 = JSONObject.toJSONString(datasource);


        String ss = "[{\"ID\":\"aaaa\",\"ddddd\":\"dddd\"},{\"ID\":\"aaaa\",\"ddddd\":\"dddd#\"}]";
        ArrayList<DbEntity> arrayList = JSONObject.parseObject(ss, ArrayList.class);

        QueryOption queryOption = new QueryOption("WFCUSTOMFORM", 0, 0);
        queryOption.setTableFilter(new TableFilter("ID", "00001HI9U1I280000A1O"));
        queryOption.setFixField("*");
        DbCollection query = dataAccess.query(queryOption);
        String s = JSONObject.toJSONString(query.getData().get(0));
        String a = "";
    }


}
