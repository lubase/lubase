package com.lcp.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.model.LookupMode;
import com.lubase.orm.model.QueryJoinCondition;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.query.LookupFieldParseService;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class RegisterColumnInfoServiceTest {

    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    LookupFieldParseService lookupFieldParseService;

    @Autowired
    DataAccess dataAccess;

    @Test
    void testInitTableInfo() {
        DbTable mainTableInfo = dataAccess.initTableInfoByTableCode("form_test");
        assert mainTableInfo.getCode().equals("form_test");
        assert mainTableInfo.getFieldList().size() == 16;
        for (DbField field : mainTableInfo.getFieldList()) {
            System.out.println(JSON.toJSONString(field));
            assert !StringUtils.isNotBlank(field.getCode());
        }
    }

    @Test
    void parseLookupFeild() {
        //(String colCode, DbTable mainTableInfo, Map<String, LookupMode> refFields, Map<String, String> joinTables
        String colCode = "ORGID.NAME";
        DbTable mainTableInfo = dataAccess.initTableInfoByTableCode("Suuser");
        Map<String, LookupMode> refFields = new HashMap<>();
        List<QueryJoinCondition> joinTables = new ArrayList<>();
        DbField field = lookupFieldParseService.parseLookupField(colCode, mainTableInfo, refFields, joinTables);

        System.out.println(String.format("colcode is %s,LOOKUP field code is %s", colCode, JSON.toJSONString(field)));
    }

    @Test
    void parseLookupFeild2() {
        //(String colCode, DbTable mainTableInfo, Map<String, LookupMode> refFields, Map<String, String> joinTables
        String colCode = "ORGID.TYPE.NAME";
        DbTable mainTableInfo = dataAccess.initTableInfoByTableCode("Suuser");
        Map<String, LookupMode> refFields = new HashMap<>();
        List<QueryJoinCondition> joinTables = new ArrayList<>();
        DbField field = lookupFieldParseService.parseLookupField(colCode, mainTableInfo, refFields, joinTables);

        System.out.println(String.format("colcode is %s,LOOKUP field code is %s", colCode, JSON.toJSONString(field)));
    }

    @Test
    void parseLookupFeild3() {
        //(String colCode, DbTable mainTableInfo, Map<String, LookupMode> refFields, Map<String, String> joinTables
        String colCode = "COLVARCHAR10.ID";
        DbTable mainTableInfo = dataAccess.initTableInfoByTableCode("FORMTEST");
        HashMap<String, LookupMode> refMap = new HashMap<>();
        LookupMode lookupMode = new LookupMode();
        lookupMode.setTableKey("CODE");
        lookupMode.setDisplayCol("NAME");
        lookupMode.setTableCode("SUUSER");
        refMap.put("COLVARCHAR10", lookupMode);
        List<QueryJoinCondition> joinTables = new ArrayList<>();
        DbField field = lookupFieldParseService.parseLookupField(colCode, mainTableInfo, refMap, joinTables);

        System.out.println(String.format("colcode is %s,LOOKUP field code is %s", colCode, JSON.toJSONString(field)));
    }


}
