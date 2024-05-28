package com.lcp.core;


import com.alibaba.fastjson.JSON;
import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.extend.IColumnRemoteService;
import com.lubase.core.model.*;
import com.lubase.core.model.DbCollection;
import com.lubase.core.model.LookupMode;
import com.lubase.core.model.QueryJoinCondition;
import com.lubase.core.model.RefLookupModel;
import com.lubase.core.operate.EOperateMode;
import com.lubase.core.service.DataAccess;
import com.lubase.core.service.query.DataAccessQueryCoreService;
import com.lubase.core.service.query.ProcessCollectionService;
import com.lubase.core.util.QueryOptionWrapper;
import com.lubase.core.util.SpringUtil;
import com.lubase.core.util.TableFilterWrapper;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataAccessTest {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;

    @Autowired
    List<ProcessCollectionService> processCollectionServices;


    @Test
    void testGetUserTable() {
        DbCollection collection = new DbCollection();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryOption queryOption = new QueryOption("form_test");
        //queryOption.setFixField("id,coldate20,coldate21");
        Object objResult = ReflectionTestUtils.invokeMethod(dataAccessQueryCoreService, "getUserTableInfo", queryOption, queryJoinTables);
        assert objResult instanceof DbTable;
        DbTable table = (DbTable) objResult;
        assert table.getFieldList().size() == 16;
        assert table.getCode().equals("form_test");

        queryOption.setFixField("id,coldate20,coldate21");
        objResult = ReflectionTestUtils.invokeMethod(dataAccessQueryCoreService, "getUserTableInfo", queryOption, queryJoinTables);
        assert objResult instanceof DbTable;
        table = (DbTable) objResult;
        assert table.getFieldList().size() == 3;
        assert table.getCode().equals("form_test");
    }

    @Test
    void testGenerateFields() {
        DbCollection collection = new DbCollection();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryOption queryOption = new QueryOption("form_test");
        queryOption.setFixField("*");
        Object objResult = ReflectionTestUtils.invokeMethod(dataAccessQueryCoreService, "generateFields", queryOption, queryJoinTables, collection);
        assert objResult instanceof String;
        String resultStr = objResult.toString();
        System.out.println(objResult.toString());
        assert resultStr.indexOf("coldate21") > 0;
        assert resultStr.indexOf("id") > 0;

        queryOption.setFixField("*");
        resultStr = ReflectionTestUtils.invokeMethod(dataAccessQueryCoreService, "generateFields", queryOption, queryJoinTables, collection);
        assert resultStr.indexOf("coldate21") > 0;
        assert resultStr.indexOf("id") > 0;
        System.out.println(objResult.toString());
    }

    @Test
    void generateJoinConditionTest() {
        String tableCode = "form_test";
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryJoinCondition q = new QueryJoinCondition();
        q.setTableAlias("suorg");
        q.setCondition("suuser.orgid=suorg.id");
        queryJoinTables.add(q);
        q = new QueryJoinCondition();
        q.setTableAlias("sscode");
        q.setCondition("orgid.type=sscode.code");
        queryJoinTables.add(q);
        Object objResult = null;
        System.out.println("starting");
        objResult = ReflectionTestUtils.invokeMethod(dataAccessQueryCoreService, "generateJoinCondition", tableCode, queryJoinTables);
        System.out.println(objResult.toString());
        assert "form_test LEFT JOIN suorg ON suuser.orgid=suorg.id LEFT JOIN sscode ON orgid.type=sscode.code ".toLowerCase().replace(" ", "")
                .equals(objResult.toString().toLowerCase().replace(" ", ""));
    }


    @Test
    void queryTestNoParams() {
        QueryOption queryOption;
        queryOption = new QueryOption("form_test", 2, 1);
        queryOption.setFixField("*");
        DbCollection collection = dataAccess.query(queryOption);
        System.out.println(collection);
        assert collection.getData().size() == 1;

    }

    @Test
    void queryTest() {
        QueryOption queryOption;
        queryOption = new QueryOption("form_test");
        queryOption.setFixField("*");
        //net.sf.ehcache.store.compound.ImmutableValueElementCopyStrategy
        DbCollection collection = dataAccess.query(queryOption);
        System.out.println(collection);
    }

    @Test
    void queryTestByLookupFilter() {
        QueryOption queryOption;
        queryOption = new QueryOption("form_test");
        queryOption.setFixField("id,collookup70,collookup70.type_name");

        TableFilter filter = new TableFilter("collookup70.type_name", "性别");
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 2;
        System.out.println(JSON.toJSONString(collection.getData().get(0)));
    }

    @Test
    void queryTestDynamicLookup() {
        String dataId = "000000399FK8G0000A01";
        QueryOption queryOption = new QueryOption("form_test");
        TableFilter filter = new TableFilter("ID", "201");
        queryOption.setTableFilter(filter);
        queryOption.setFixField("colint40,colvarchar10.id,colvarchar10.table_code,colvarchar10.table_name");
        HashMap<String, LookupMode> refMap = new HashMap<>();
        LookupMode lookupMode = new LookupMode("table_code", "table_name", "dm_table");
        refMap.put("colvarchar10", lookupMode);
        queryOption.setRefFields(refMap);
        DbCollection collection2 = dataAccess.query(queryOption);
        assert collection2.getData().size() == 1;
        System.out.println(JSON.toJSONString(collection2.getData().get(0)));
        for (DbField field : collection2.getTableInfo().getFieldList()) {
            System.out.println(String.format("%s___%s", field.getCode(), field.getTableCode()));
        }
    }


    /**
     * 有id
     */
    @Test
    void queryEditDataTest() {
        //DbCollection dbCollection = dataAccess.queryById("dm_table", "2022052817099649643");
        DbCollection dbCollection = dataAccess.queryById("dm_table", 2022052817099649643L);
        String s = JSON.toJSONString(dbCollection);
        System.out.println(s);
        assert dbCollection.getTotalCount() == 1;
    }

    /**
     * 没有有id
     */
    @Test
    void queryEditDataNoIdTest() {
        DbCollection dbCollection = dataAccess.queryById("dm_table", 2123212L);
        String s = JSON.toJSONString(dbCollection);
        System.out.println(s);
        assert dbCollection.getTotalCount() == 0;
    }

    @Test
    void queryRefCodeDataDanXuan() {
        TableFilter filter = TableFilterWrapper.and().eq("id", "204").build();
        QueryOption queryOption = QueryOptionWrapper.select("colcodedata60").from("form_test").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getData().size() == 1;
        DbEntity entity = collection.getData().get(0);
        assert entity.get("colcodedata60").toString().equals("1");
        HashMap<String, String> refData = entity.getRefData();
        assert refData != null;
        System.out.println(entity);
        System.out.println(JSON.toJSONString(entity));
        assert refData.containsKey("colcodedata60NAME") && StringUtils.isNotBlank(refData.get("colcodedata60NAME"));
    }

    @Test
    void queryRefCodeDataDuoXuan() {
        TableFilter filter = TableFilterWrapper.and().eq("id", "204").build();
        QueryOption queryOption = QueryOptionWrapper.select("colcodedata61").from("form_test").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getData().size() == 1;
        DbEntity entity = collection.getData().get(0);
        assert entity.get("colcodedata61").toString().equals("441500,350100");
        HashMap<String, String> refData = entity.getRefData();
        assert refData != null;
        System.out.println(entity);
        System.out.println(JSON.toJSONString(entity));
        assert refData.containsKey("colcodedata61NAME") && StringUtils.isNotBlank(refData.get("colcodedata61NAME"));
    }

    @Test
    void queryRefLookupDataDanXuan() {
        QueryOption queryOption;
        queryOption = new QueryOption("form_test");
        queryOption.setFixField("collookup70");

        TableFilter filter = new TableFilter("id", "201");
        queryOption.setTableFilter(filter);
        queryOption.setBuildLookupField(true);
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 1;
        DbEntity entity = collection.getData().get(0);
        System.out.println(entity);

        for (DbField colInfo : collection.getTableInfo().getFieldList()) {
            LookupMode lookupMode = LookupMode.FromJsonStr(colInfo.getLookup());
            //判断不处理情况
            System.out.println(lookupMode);
            if (!queryOption.isBuildLookupField() ||
                    lookupMode == null ||
                    !colInfo.getEleType().equals("7") ||
                    colInfo.getIsMultivalued() == 1 ||
                    lookupMode.getTableKey() == lookupMode.getDisplayCol()) //关联字段与显示字段一致
            {
                System.out.println("code is " + colInfo.getCode());
            }
        }

        assert entity.containsKey("collookup70");
        assert !entity.containsKey("collookup70NAME");
        System.out.println(entity);
    }

    @Test
    void testSortFieldPaging() {
        TableFilter filter = new TableFilter("code_type_id", "2022052817208000285", EOperateMode.Equals);
        QueryOption queryOption = QueryOptionWrapper.select("*").from("dm_code").where(filter).build();
        queryOption.setQueryMode(2);
        DbCollection collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(JSON.toJSONString(entity));
        }
        System.out.println("***********11111111111desc");
        queryOption.setSortField("order_id desc");
        collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(JSON.toJSONString(entity));
        }
        System.out.println("***********222222");
        queryOption.setSortField("order_id ASC");
        queryOption.addAscSort("order_id");
        collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(JSON.toJSONString(entity));
        }
    }

    @Test
    void testSortFieldNoPaging() {
        TableFilter filter = new TableFilter("code_type_id", "2022052817208000285", EOperateMode.Equals);
        QueryOption queryOption = QueryOptionWrapper.select("*").from("dm_code").where(filter).build();
        queryOption.setQueryMode(2);
        DbCollection collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(JSON.toJSONString(entity));
        }
        System.out.println("***********11111111111desc");
        queryOption.setSortField("order_id desc");
        collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(JSON.toJSONString(entity));
        }
        System.out.println("***********222222");
        queryOption.setSortField("order_id ASC");
        queryOption.addAscSort("order_id");
        collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(JSON.toJSONString(entity));
        }
    }

    @Test
    void testMaultiValue() {
        QueryOption queryOption = QueryOptionWrapper.select("*").from("form_test").build();
        queryOption.setFixField("collookup71");
        DbCollection collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(entity.getRefData());
        }
    }

    @Test
    void testDataFormat() {
        QueryOption queryOption = QueryOptionWrapper.select("*").from("form_test").build();
        queryOption.setFixField("coldate20,coldate21");
        DbCollection collection = dataAccess.query(queryOption);
        for (DbEntity entity : collection.getData()) {
            System.out.println(entity);
        }
    }

    @Test
    void testServiceColumn() {
        QueryOption queryOption = new QueryOption("ipp_rdvehicle_info");
        queryOption.setFixField("seniorminister");
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() > 0) {
            System.out.println(collection.getData().get(0));
        }
    }

    @Test
    void testService() {
        IColumnRemoteService service = null;
        try {
            String serviceName = "userColumnServiceTestImpl";
            service = (IColumnRemoteService) SpringUtil.getApplicationContext().getBean(serviceName);
            System.out.println("开始执行getAlLData");
            System.out.println(service.getAllData());
        } catch (Exception ex) {
            // throw new InvokeServiceNotFoundException(componentPath,"IFormTrigger");
            System.out.println("service is null" + ex.getMessage());
        }
    }

    @Test
    void testFilterHasLookupField() {
        QueryOption queryOption = new QueryOption("ss_button");
        queryOption.setFixField("button_name,page_id.page_name");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("page_id.page_name", "应用管理");
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.query(queryOption);
        System.out.println(JSON.toJSONString(collection));
    }

    @Test
    void testProcessData() {
        for (ProcessCollectionService service : processCollectionServices) {
            System.out.println(service.getClass().toString());
        }
        assert processCollectionServices.size() == 5;
    }

    @Test
    void testExtendColumnDisplay() {
        QueryOption queryOption = new QueryOption("ss_app");
        queryOption.setFixField("app_owner");
        DbCollection collection = dataAccess.query(queryOption);
        System.out.println(JSON.toJSONString(collection.getData()));
        assert collection.getData().size() == 3;
    }

    @Test
    void testQueryOtherDB() {
        QueryOption queryOption = new QueryOption("ipp_inte_vehicle");
        queryOption.setFixField("id,rdps_project,inte_director,file_key");
        DbCollection collection = dataAccess.query(queryOption);
        System.out.println(JSON.toJSONString(collection.getData()));
        //assert collection.getData().size() == 3;
    }

    @Test
    void testQueryConditionIsNull() {
        QueryOption queryOption = new QueryOption("form_test");
        TableFilter filter = new TableFilter("coldate20", null, EOperateMode.IsNull);
        queryOption.setTableFilter(filter);
        queryOption.setBuildLookupField(false);
        DbCollection collection = dataAccess.queryAllData(queryOption);
        System.out.println(collection.getData().size());
        assert collection.getData().size() == 33;
    }

    @Test
    void testIsNullAndEmpty() {
        QueryOption queryOption = new QueryOption("ss_app");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("config_admin", "@@S.empty");
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection coll = dataAccess.queryAllData(queryOption);
        System.out.println(coll.getData().size());

        queryOption = new QueryOption("ss_app");
        filterWrapper = TableFilterWrapper.and();
        filterWrapper.isNull("config_admin");
        queryOption.setTableFilter(filterWrapper.build());
        coll = dataAccess.queryAllData(queryOption);
        System.out.println(coll.getData().size());

        queryOption = new QueryOption("ss_app");
        filterWrapper = TableFilterWrapper.or();
        filterWrapper.isNull("config_admin").eq("config_admin", "@@S.empty");
        queryOption.setTableFilter(filterWrapper.build());
        coll = dataAccess.queryAllData(queryOption);
        System.out.println(coll.getData().size());

    }

    @Test
    void testSort() {
        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setSortField("id desc");
        Map<String, LookupMode> map = new HashMap<>();
        map.put("organization_id", new LookupMode("id", "org_name", "sa_organization"));
        queryOption.setRefFields(map);
        queryOption.setFixField("organization_id.org_name,user_code,user_name");
        queryOption.setPageIndex(1);
        queryOption.setPageSize(1);
        DbCollection coll = dataAccess.query(queryOption);
        System.out.println(JSON.toJSONString(coll.getData()));
    }

    @Test
    void testOnlyCodeField() {
        QueryOption queryOption = new QueryOption("wf_fins");
        queryOption.setFixField("service_name,name,approval_status");
        queryOption.setTableFilter(new TableFilter("id", "921057989824090112"));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        DbEntity entity = collection.getData().get(0);

        System.out.println(JSON.toJSONString(entity));
    }


    @Test
    void testGetCodeData() {
        String typeId = "668931905378324480";
        List<DbCode> list = dataAccess.getCodeListByTypeId(typeId);
        System.out.println(JSON.toJSONString(list));
        typeId = "abcd";
        list = dataAccess.getCodeListByTypeId(typeId);
        System.out.println(JSON.toJSONString(list));
    }


    @Test
    void testAddData() {
        DbCollection coll = dataAccess.getEmptyData("dm_code_type");
        DbEntity entity = coll.newEntity();
        entity.put("app_id", "673605666534854656");
        entity.put("type_name", "testData");
        entity.put("category", 1);
        entity.put("order_id", 3);
        entity.put("client_use", 1);
        coll.getData().add(entity);
        dataAccess.update(coll);
    }

    @Test
    void testLookup() {
        QueryOption queryOption = new QueryOption("bj_student");
        queryOption.setFixField("id,name,logo,bj_demo_id,bj_demo_id.logo,bj_demo_id.nianji");
        queryOption.setPageSize(1);
        queryOption.setPageIndex(1);
        DbCollection coll = dataAccess.query(queryOption);
        System.out.println(JSON.toJSONString(coll));
    }

    @Test
    void testLookupData() {
        QueryOption queryOption = new QueryOption("dm_column");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("ele_type", "7");
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection coll = dataAccess.queryAllData(queryOption);

        for (DbEntity entity : coll.getData()) {
            String lookupStr = TypeConverterUtils.object2String(entity.get("lookup"), "");
            if (StringUtils.isBlank(lookupStr)) {
                continue;
            }
            LookupMode lookupMode = LookupMode.FromJsonStr(lookupStr);
            RefLookupModel refModel = new RefLookupModel();
            refModel.setTableKey(lookupMode.getTableKey());
            refModel.setDisplayCol(lookupMode.getDisplayCol());
            refModel.setExtendCol(lookupMode.getExtendCol());
            refModel.setSearchCols(lookupMode.getSearchCols());
            refModel.setSelectCols(lookupMode.getSelectCols());
            refModel.setPageId(lookupMode.getPageId());

            entity.put("lookup", JSON.toJSONString(refModel));
        }
        dataAccess.update(coll);

    }

    // 根据表代码获取表类型
    String testGetTableType(String tableCode) {
        QueryOption queryOption = new QueryOption("dm_table");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq("table_code", tableCode);
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection coll = dataAccess.queryAllData(queryOption);

        return coll.getData().get(0).get("table_type_id").toString();

    }
}