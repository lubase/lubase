package com.lcp.qibao;

import com.lubase.orm.service.DataAccess;
import com.lubase.core.util.InvokeDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SqlDataSourceTest {

    @Autowired
    InvokeDataSourceService sqlDataSource;

    @Autowired
    DataAccess dataAccess;
//
//    @Test
//    void testSql1() {
//        //sql 返回对象  1 参数
//        // Object obj = sqlDataSource.queryObjectByDataSource("00001PZ67YJLI0000A10", "00001PZ67YJLI0000A10");
//        Object obj = sqlDataSource.queryObjectByDataSource(11111111111111L, 0L, "00001PZ67YJLI0000A10");
//        assert obj instanceof DbEntity;
//        ((DbEntity) obj).getId().equals("00001PZ67YJLI0000A10");
//    }
//
//    @Test
//    void testSql2() {
//        //sql 返回列表,无参数
//        // Object obj = sqlDataSource.queryObjectByDataSource("00001PZ67YJLI0000A11");
//        Object obj = sqlDataSource.queryObjectByDataSource(111111111111111111L, 0L);
//        assert obj instanceof ArrayList;
//        QueryOption queryOption = new QueryOption("SSDATASOURCE");
//        queryOption.setQueryMode(2);
//        DbCollection collection = dataAccess.query(queryOption);
//        assert collection.getTotalCount() > 0;
//        assert (((ArrayList<?>) obj)).size() == collection.getTotalCount();
//    }
//
//    @Test
//    void testQuery1() {
//        //sql 返回对象  1 参数
//        //Object obj = sqlDataSource.queryObjectByDataSource("00001PZ67YJLI0000A15", "00001PZ67YJLI0000A15");
//        Object obj = sqlDataSource.queryObjectByDataSource(1L, 0L, "00001PZ67YJLI0000A15");
//        assert obj instanceof DbEntity;
//        ((DbEntity) obj).getId().equals("00001PZ67YJLI0000A15");
//    }
//
//    @Test
//    void testQuery2() {
//        //sql 返回对象  1 参数
//        // Object obj = sqlDataSource.queryObjectByDataSource("00001PZ67YJLI0000A16", "00001PZ67YJLI0000A16");
//        Object obj = sqlDataSource.queryObjectByDataSource(11L, 0L, "00001PZ67YJLI0000A16");
//        assert obj instanceof ArrayList;
//        assert (((ArrayList<?>) obj)).size() == 1;
//    }
//
//    @Test
//    void testQuery3() {
//        //sql 返回DBcollection ,1 参数
//        //Object obj = sqlDataSource.queryObjectByDataSource("00001PZ67YJLI0000A13", "00001PZ67YJLI0000A13");
//        Object obj = sqlDataSource.queryObjectByDataSource(11L, 0L, "00001PZ67YJLI0000A13");
//        assert obj instanceof DbCollection;
//        DbCollection collection = (DbCollection) obj;
//        assert collection.getTotalCount() == 1;
//        assert collection.getData().get(0).getId().equals("00001PZ67YJLI0000A13");
//    }
//
//    @Test
//    void testQuery4() {
//        //sql 返回表结构，无参数
//        //Object obj = sqlDataSource.queryObjectByDataSource("00001PZ67YJLI0000A14", "00001PZ67YJLI0000A14");
//        Object obj = sqlDataSource.queryObjectByDataSource(11L, 0L, "00001PZ67YJLI0000A14");
//        assert obj instanceof ArrayList;
//        ArrayList<DbField> list = (ArrayList<DbField>) obj;
//        QueryOption queryOption = new QueryOption("SSDATASOURCE");
//        queryOption.setQueryMode(2);
//        queryOption.setTableFilter(new TableFilter("ID", "xxx", EOperateMode.Equals));
//        DbCollection collection = dataAccess.query(queryOption);
//        assert collection.getTableInfo().getFieldList().size() == list.size();
//        System.out.println(JSON.toJSONString(list));
//    }
//
//    @Test
//    void testLeftData() {
//        //List<DbEntity> list = sqlDataSource.queryListBySql("00000039VBDUW0000A0I");
//        List<DbEntity> list = sqlDataSource.queryListBySql(0L, 1L);
//        QueryOption queryOption = new QueryOption("SSCTYPE");
//        queryOption.setQueryMode(2);
//        DbCollection collection = dataAccess.query(queryOption);
//        queryOption = new QueryOption("SSCODE");
//        queryOption.setQueryMode(2);
//        queryOption.setTableFilter(new TableFilter("TYPEID", "00001H8I753R40000A0F", EOperateMode.Equals));
//        DbCollection collection2 = dataAccess.query(queryOption);
//        assert list.size() == (collection.getTotalCount() + collection2.getTotalCount());
//        System.out.println(JSON.toJSONString(list));
//    }
//
//    @Test
//    void testMainData() {
//        // DbCollection collection = sqlDataSource.queryDBCollectionBySql("00001PZ67YJLI0000A13", "00001PZ67YJLI0000A13");
//        DbCollection collection = sqlDataSource.queryDBCollectionBySql(11L, 12313L);
//        assert collection.getTotalCount() == 1;
//        assert collection.getData().get(0).getId().equals("00001PZ67YJLI0000A13");
//    }
}
