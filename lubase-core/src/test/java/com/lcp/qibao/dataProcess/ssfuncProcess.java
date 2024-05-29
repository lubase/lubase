package com.lcp.qibao.dataProcess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.QueryOption;
import com.lubase.orm.util.QueryOptionWrapper;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.TableFilter;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ssfuncProcess {

    @Autowired
    DataAccess dataAccess;

    @Test
    void test1() {
        TableFilter filter = TableFilterWrapper.and().eq("CODE", "010103").build();
        QueryOption queryOption = QueryOptionWrapper.select("*").from("SSPAGE").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);

        String paramsStr = "{\"MasterPage\":\"1101\",\"Url\":\"\",\"GridInfo\":\"{\\\"ajaxGlobal\\\":true,\\\"disAdd\\\":true,\\\"disEdit\\\":true,\\\"disDelete\\\":true,\\\"disRefresh\\\":true,\\\"disSave\\\":true,\\\"disImport\\\":true,\\\"disExport\\\":true,\\\"disFieldSet\\\":true,\\\"disQuery\\\":true,\\\"disToolbar\\\":true,\\\"disLock\\\":true,\\\"disPage\\\":true,\\\"disPageDes\\\":true,\\\"enableCheckBox\\\":true,\\\"enableHover\\\":true,\\\"enableSelect\\\":true,\\\"changeRowColor\\\":true,\\\"selectFirst\\\":false,\\\"pageNumShown\\\":\\\"10\\\",\\\"callback\\\":{}}\",\"GridQuery\":\"{\\\"TableName\\\":\\\"SSFUNCITEM\\\",\\\"QueryType\\\":\\\"1\\\",\\\"Sort\\\":\\\"ID DESC\\\",\\\"TableFilter\\\":{\\\"FilterDisplay\\\":\\\"过滤条件设置\\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":null,\\\"FilterValue\\\":null,\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"3\\\",\\\"ChildFilters\\\":[]},\\\"TableType\\\":\\\"000005FOAS2OU0000154\\\",\\\"PageSize\\\":\\\"10\\\",\\\"TableTypeNAME\\\":\\\"系统表\\\",\\\"TableNameNAME\\\":\\\"原子功能表SSFUNCITEM\\\",\\\"QueryTypeNAME\\\":\\\"分页查询\\\",\\\"FixField\\\":\\\"\\\",\\\"FixFieldNAME\\\":\\\"\\\",\\\"SortNAME\\\":\\\"主键(降序)\\\"}\",\"CustomParams\":\"\",\"TreeInfo\":\"\",\"TreeQuery\":\"\",\"SearchFilter\":\"{\\\"TableName\\\":\\\"SSFUNCITEM\\\",\\\"PageIndex\\\":1,\\\"PageSize\\\":10,\\\"QueryType\\\":\\\"2\\\",\\\"FixField\\\":\\\"\\\",\\\"GroupField\\\":null,\\\"Sort\\\":\\\"\\\",\\\"Mode\\\":1,\\\"TableFilter\\\":{\\\"FilterDisplay\\\":\\\"过滤条件设置\\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":null,\\\"FilterValue\\\":null,\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"3\\\",\\\"ChildFilters\\\":[{\\\"FilterDisplay\\\":\\\"1\\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":\\\"\\\",\\\"FilterValue\\\":null,\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"3\\\",\\\"ChildFilters\\\":[{\\\"FilterDisplay\\\":\\\"主键 %全匹配% \\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":\\\"ID\\\",\\\"FilterValue\\\":\\\"\\\",\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"0\\\",\\\"ChildFilters\\\":null},{\\\"FilterDisplay\\\":\\\"原子功能名称 %全匹配% \\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":\\\"NAME\\\",\\\"FilterValue\\\":\\\"\\\",\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"0\\\",\\\"ChildFilters\\\":null},{\\\"FilterDisplay\\\":\\\"原子功能路径 %全匹配% \\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":\\\"PATH\\\",\\\"FilterValue\\\":\\\"\\\",\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"0\\\",\\\"ChildFilters\\\":null}]}]},\\\"TableType\\\":\\\"000005FOAS2OU0000154\\\",\\\"TableTypeNAME\\\":\\\"系统表\\\",\\\"TableNameNAME\\\":\\\"原子功能表SSFUNCITEM\\\",\\\"QueryTypeNAME\\\":\\\"全部查询\\\",\\\"FixFieldNAME\\\":\\\"\\\",\\\"SortNAME\\\":\\\"\\\"}\",\"SearchHtml\":\"<div class=\\\"searchbar\\\"><table><tbody><tr><td id=\\\"emptyTd\\\" colspan=\\\"6\\\"></td><td rowspan=\\\"10\\\"><button type=\\\"submit\\\" class=\\\"btn btn-primary\\\">查询</button><button type=\\\"reset\\\" class=\\\"btn btn-default\\\">清空</button></td></tr><tr><td class=\\\"right\\\">主键</td><td><input data-ele=\\\"0\\\" class=\\\"width1\\\" placeholder=\\\"\\\" data-bind=\\\"path00\\\" name=\\\"path00\\\" regular=\\\"\\\" errormsg=\\\"\\\" type=\\\"TEXT\\\"></td><td class=\\\"right\\\">原子功能名称</td><td><input data-ele=\\\"0\\\" class=\\\"width1\\\" placeholder=\\\"\\\" data-bind=\\\"path01\\\" name=\\\"path01\\\" regular=\\\"\\\" errormsg=\\\"\\\" type=\\\"TEXT\\\"></td><td class=\\\"right\\\">原子功能路径</td><td><input data-ele=\\\"0\\\" class=\\\"width1\\\" placeholder=\\\"\\\" data-bind=\\\"path02\\\" name=\\\"path02\\\" regular=\\\"\\\" errormsg=\\\"\\\" type=\\\"TEXT\\\"></td></tr></tbody></table></div><legend style=\\\"margin-bottom:0px;\\\"></legend>\"}";
        for (DbEntity entity : collection.getData()) {
            //String paramsStr = entity.get("PARMS").toString();
            if (StringUtils.isBlank(paramsStr)) {
                continue;
            }
            JSONObject jsonObject = JSON.parseObject(paramsStr);
            setValue(entity, "MasterPage", jsonObject);
            setValue(entity, "Url", jsonObject);
            setValue(entity, "GridInfo", jsonObject);
            setValue(entity, "GridQuery", jsonObject);
            setValue(entity, "CustomParams", jsonObject);
            setValue(entity, "SearchFilter", jsonObject);
            setValue(entity, "TreeInfo", jsonObject);
            setValue(entity, "TreeQuery", jsonObject);
        }
        dataAccess.update(collection);
    }

    void setValue(DbEntity entity, String colCode, JSONObject jsonObject) {
        if (jsonObject.containsKey(colCode)) {
            entity.put(colCode.toUpperCase(), jsonObject.get(colCode));
        }
    }

    @Test
    void testDuoZhiLookup() {
        QueryOption queryOption = new QueryOption("cpt_main_task");
        queryOption.setTableFilter(new TableFilter("id", "696262657941966848"));
        queryOption.setFixField("related_person");
        DbCollection collection = dataAccess.queryAllData(queryOption);
        assert collection.getData().size() == 1;
        DbEntity entity = collection.getData().get(0);
        System.out.println(entity);
    }
}
