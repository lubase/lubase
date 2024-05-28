package com.lcp.qibao.tool;

import com.alibaba.fastjson.JSON;
import com.lubase.core.service.DataAccess;
import com.lubase.core.QueryOption;
import com.lubase.core.util.QueryOptionWrapper;
import com.lubase.core.model.DbCollection;
import com.lubase.core.TableFilter;
import com.lubase.core.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.starter.auto.entity.SsPageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DataAccessQueryTest {

    @Autowired
    DataAccess dataAccess;

    @Test
    void testGenericDataQuery() {
        TableFilter filter = TableFilterWrapper.and().eq("CODE", "010101").build();
        QueryOption queryOption = QueryOptionWrapper.select("*").from("SSPAGE").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);

        DbEntity entity = collection.getData().get(0);
        System.out.println(entity.getClass().getName());
        System.out.println(JSON.toJSONString(entity));

        List<SsPageEntity> list = collection.getGenericData(SsPageEntity.class);
        SsPageEntity sspage = list.get(0);
        System.out.println(sspage.getClass().getName());
        System.out.println(JSON.toJSONString(sspage));

        assert JSON.toJSONString(entity).equals(JSON.toJSONString(sspage));
    }

    @Test
    void testGenericDataUpdate() {
        TableFilter filter = TableFilterWrapper.and().eq("CODE", "010101").build();
        QueryOption queryOption = QueryOptionWrapper.select("*").from("SSPAGE").where(filter).build();
        DbCollection collection = dataAccess.query(queryOption);

        //注意：从标准collection对象获取强类型的列表
        List<SsPageEntity> list = collection.getGenericData(SsPageEntity.class);
        SsPageEntity sspage = list.get(0);
        sspage.setDescription("test update for des");
        assert sspage.getDataState().equals(EDBEntityState.Modified);
        // 注意：此行不能少，将强类型的读写转换成标准的对象
        collection.setGenericData(list);

        assert 1 == dataAccess.update(collection);
    }

}
