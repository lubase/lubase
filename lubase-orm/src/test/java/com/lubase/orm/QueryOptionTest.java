package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.TableFilter;
import com.lubase.orm.operate.EOperateMode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QueryOptionTest {

    @Test
    void testFanXuLieHua() {
//        String str = "{\"MasterPage\":\"1110\",\"Url\":\"System/FuncManager\",\"GridInfo\":\"\",\"GridQuery\":\"\",\"CustomParams\":\"\",\"TreeInfo\":\"{\\\"enableAdd\\\":false,\\\"enableEdit\\\":false,\\\"enableDel\\\":false,\\\"enableSearch\\\":true,\\\"simpleData\\\":true,\\\"keyField\\\":\\\"CODE\\\",\\\"displayFields\\\":[\\\"NAME\\\",\\\"CODE\\\"],\\\"relationField\\\":\\\"PCODE\\\",\\\"selectedMulti\\\":false,\\\"callback\\\":{\\\"beforeClick\\\":\\\"treeBeforeClick\\\",\\\"beforeBind\\\":\\\"treebeforeBind\\\"}}\",\"TreeQuery\":\"{\\\"TableName\\\":\\\"SSFUNC\\\",\\\"PageIndex\\\":1,\\\"PageSize\\\":10,\\\"QueryType\\\":\\\"2\\\",\\\"FixField\\\":\\\"\\\",\\\"GroupField\\\":null,\\\"Sort\\\":\\\"PCODE ASC,ORDERID ASC\\\",\\\"Mode\\\":1,\\\"TableFilter\\\":{\\\"FilterDisplay\\\":\\\"过滤条件设置\\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":null,\\\"FilterValue\\\":null,\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"3\\\",\\\"ChildFilters\\\":[{\\\"FilterDisplay\\\":\\\"类型 小于等于 3\\\",\\\"FilterTip\\\":\\\"\\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":\\\"TYPE\\\",\\\"FilterValue\\\":\\\"3\\\",\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"8\\\",\\\"ChildFilters\\\":null},{\\\"FilterDisplay\\\":\\\"可见性 等于 1\\\",\\\"FilterTip\\\":\\\"\\\",\\\"And\\\":true,\\\"Not\\\":false,\\\"FilterName\\\":\\\"DIS\\\",\\\"FilterValue\\\":\\\"1\\\",\\\"FilterValueNAME\\\":null,\\\"OperateMode\\\":\\\"3\\\",\\\"ChildFilters\\\":null}]},\\\"TableType\\\":\\\"000005FOAS2OU0000154\\\",\\\"TableTypeNAME\\\":\\\"系统表\\\",\\\"TableNameNAME\\\":\\\"系统功能表\\\",\\\"QueryTypeNAME\\\":\\\"全部查询\\\",\\\"FixFieldNAME\\\":\\\"\\\",\\\"SortNAME\\\":\\\"上级代码(升序),排序号(升序)\\\"}\",\"SearchFilter\":\"\",\"ISMENUVISIBLE\":0}";
//        PageParams pageParams = JSON.parseObject(str, PageParams.class);
//        System.out.println(pageParams.getTreeQuery());
//        QueryOption serverQuery = JSON.parseObject(pageParams.getTreeQuery(), QueryOption.class);
//        String str2 = JSON.toJSONString(serverQuery);
//        System.out.println(str2);
//
//
//        String str3 = "{\"TableName\":\"SSFUNC\",\"PageIndex\":1,\"PageSize\":10,\"QueryType\":\"2\",\"FixField\":\"\",\"GroupField\":null,\"Sort\":\"PCODE ASC,ORDERID ASC\",\"Mode\":1,\"TableFilter\":{\"FilterDisplay\":\"过滤条件设置\",\"And\":true,\"Not\":false,\"FilterName\":null,\"FilterValue\":null,\"FilterValueNAME\":null,\"OperateMode\":3,\"ChildFilters\":[{\"FilterDisplay\":\"类型 小于等于 3\",\"FilterTip\":\"\",\"And\":true,\"Not\":false,\"FilterName\":\"TYPE\",\"FilterValue\":\"3\",\"FilterValueNAME\":null,\"OperateMode\":\"8\",\"ChildFilters\":null},{\"FilterDisplay\":\"可见性 等于 1\",\"FilterTip\":\"\",\"And\":true,\"Not\":false,\"FilterName\":\"DIS\",\"FilterValue\":\"1\",\"FilterValueNAME\":null,\"OperateMode\":\"3\",\"ChildFilters\":null}]},\"TableType\":\"000005FOAS2OU0000154\",\"TableTypeNAME\":\"系统表\",\"TableNameNAME\":\"系统功能表\",\"QueryTypeNAME\":\"全部查询\",\"FixFieldNAME\":\"\",\"SortNAME\":\"上级代码(升序),排序号(升序)\"}";
//        System.out.println(JSON.toJSONString(JSON.parseObject(str3, QueryOption.class)));

    }

    @Test
    void tableFilterTest() {
        TableFilter filter = new TableFilter();
        filter.setFilterValue("3");
        filter.setFilterName("type");
        filter.setOperateMode(EOperateMode.LessEquals);
        System.out.println(JSON.toJSONString(filter));

        String str = "{\"and\":true,\"filterName\":\"type\",\"filterValue\":\"3\",\"not\":false,\"operateMode\":\"LessEquals\"}";
        TableFilter filter1 = JSON.parseObject(str, TableFilter.class);
        System.out.println(JSON.toJSONString(filter1));
    }
}
