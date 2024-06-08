package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LookupMode;
import com.lubase.orm.model.QueryJoinCondition;
import com.lubase.orm.model.QueryParamEntity;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.query.TableFilterBuilder;
import com.lubase.orm.util.QueryOptionWrapper;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TableFilterBuilderTest {
    @Autowired
    TableFilterBuilder tableFilterBuilder;
    @Autowired
    DataAccess dataAccess;

    @Test
    void buildSql() {
        TableFilter filter = getTableFilter();
        DbTable tableInfo = dataAccess.initTableInfoByTableCode("form_test");
        String tableAlias = "form_test";
        QueryParamEntity queryParam = new QueryParamEntity();
        Map<String, LookupMode> refField = new Hashtable<>();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        System.out.println(String.format("queryJoinTables size is  %s", queryJoinTables.size()));
        String sql = tableFilterBuilder.parseTableFilterToStr(filter, tableInfo, tableAlias, queryParam, refField, queryJoinTables);
        System.out.println(sql);
        System.out.println(String.format("queryJoinTables size is  %s", queryJoinTables.size()));
    }

    @Test
    void buildSqlWithCompareMode() {
        TableFilter filter = getTableFilter();
        DbTable tableInfo = dataAccess.initTableInfoByTableCode("form_test");
        String tableAlias = "form_test";
        QueryParamEntity queryParam = new QueryParamEntity();
        Map<String, LookupMode> refField = new Hashtable<>();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryJoinCondition queryJoinCondition = new QueryJoinCondition();
        queryJoinCondition.setTableAlias("\"SUORG\" AS \"ORGID\"");
        queryJoinCondition.setCondition("");
        queryJoinTables.add(queryJoinCondition);
        String sql = tableFilterBuilder.parseTableFilterToStr(filter, tableInfo, tableAlias, queryParam, refField, queryJoinTables);

        System.out.println(String.format("filter is %s", JSON.toJSON(filter)));
        System.out.println(sql);
        System.out.println(String.format("parameters is %s", JSON.toJSON(queryParam)));
        System.out.println(String.format("parameters is %s", JSON.toJSON(queryParam)));

    }

    @Test
    void test2() {
        TableFilter filter = TableFilterWrapper.or().eq("ID", "id11").eq("ID", "id11").build();
        DbTable tableInfo = dataAccess.initTableInfoByTableCode("suuser");
        String tableAlias = "suuser";
        QueryParamEntity queryParam = new QueryParamEntity();
        Map<String, LookupMode> refField = new Hashtable<>();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        QueryJoinCondition queryJoinCondition = new QueryJoinCondition();
        queryJoinCondition.setTableAlias("\"SUORG\" AS \"ORGID\"");
        queryJoinCondition.setCondition("");
        queryJoinTables.add(queryJoinCondition);
        String sql = tableFilterBuilder.parseTableFilterToStr(filter, tableInfo, tableAlias, queryParam, refField, queryJoinTables);
        System.out.println(String.format("filter is %s", JSON.toJSON(filter)));
        System.out.println(sql);
        System.out.println(String.format("parameters is %s", JSON.toJSON(queryParam)));
        System.out.println(String.format("parameters is %s", JSON.toJSON(queryParam)));
    }

    TableFilter getTableFilter() {
        TableFilter filter1 = new TableFilter();
        filter1.setFilterName("CODE");
        filter1.setFilterValue("dg007");
        filter1.setOperateMode(EOperateMode.Equals);

        TableFilter filter2 = new TableFilter();
        filter2.setFilterName("ORGID.NAME");
        filter2.setFilterValue("张三");
        filter2.setOperateMode(EOperateMode.Equals);

        TableFilter filter = new TableFilter();
        filter.setChildFilters(new ArrayList<>());
        filter.getChildFilters().add(filter1);
        filter.getChildFilters().add(filter2);
        return filter;
    }


    String buildSqlByFilter(TableFilter filter) {
        DbTable tableInfo = dataAccess.initTableInfoByTableCode("sa_account");
        String tableAlias = "sa_account";
        QueryParamEntity queryParam = new QueryParamEntity();
        Map<String, LookupMode> refField = new Hashtable<>();
        List<QueryJoinCondition> queryJoinTables = new ArrayList<>();
        String sql = tableFilterBuilder.parseTableFilterToStr(filter, tableInfo, tableAlias, queryParam, refField, queryJoinTables);
        return sql;
    }

    @Test
    void testEq() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.Equals);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.code=#{code}".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testNotEq() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.NotEquals);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.code<>#{code}".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testGreat() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.Great);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.code>#{code}".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testGreatEq() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.GreateEquals);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.code>=#{code}".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testLess() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.Less);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.code<#{code}".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testLessEq() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.LessEquals);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.code<=#{code}".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testIsNull() {
        TableFilter filter = new TableFilter("user_code", null, EOperateMode.IsNull);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codeisnull".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testIsNotNull() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.IsNotNull);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codeisnotnull".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testIn() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.In);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codein(#{code})".equals(sql.toLowerCase().replace(" ", ""));

        filter = new TableFilter("CODE", "admin,Develper", EOperateMode.In);
        sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codein(#{code},#{code1})".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testnotIn() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.NotIn);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codenotin(#{code})".equals(sql.toLowerCase().replace(" ", ""));

        filter = new TableFilter("CODE", "admin,Develper", EOperateMode.NotIn);
        sql = buildSqlByFilter(filter);
        System.out.println(sql.toLowerCase().replace(" ", ""));
        assert "suuser.codenotin(#{code},#{code1})".equals(sql.toLowerCase().replace(" ", ""));
    }

    @Test
    void testLikeLeft() {
        String exp = "%s like '%s%s'";
        String str2 = String.format(exp, "aa", "bb", "%");
        System.out.println(str2);

        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.LikeLeft);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codelike#{code}".equals(sql.toLowerCase().replace(" ", ""));

        QueryOption queryOption = new QueryOption("SSFUNC", 1, 10000);
        queryOption.setFixField("ID");
        filter = TableFilterWrapper.and().likeLeft("CODE", "01").build();
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 146;
    }

    @Test
    void testLikeRight() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.LikeLeft);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codelike#{code}".equals(sql.toLowerCase().replace(" ", ""));

        filter = TableFilterWrapper.and().likeRight("CODE", "02").build();
        QueryOption queryOption = QueryOptionWrapper.select("ID").from("SSFUNC").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 6;
    }

    @Test
    void testLikeAll() {
        TableFilter filter = new TableFilter("CODE", "admin", EOperateMode.LikeLeft);
        String sql = buildSqlByFilter(filter);
        System.out.println(sql);
        assert "suuser.codelike#{code}".equals(sql.toLowerCase().replace(" ", ""));

        filter = TableFilterWrapper.and().likeAll("CODE", "02").build();
        QueryOption queryOption = QueryOptionWrapper.select("ID").from("SSFUNC").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);
        assert collection.getTotalCount() == 44;
    }
}
