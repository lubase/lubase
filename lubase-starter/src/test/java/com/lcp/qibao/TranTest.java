package com.lcp.qibao;

import com.lubase.core.service.DataAccess;
import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.operate.EOperateMode;
import com.lubase.core.TableFilter;
import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@SpringBootTest
public class TranTest {
    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    DataAccess dataAccess;

    @Test
    void testUpdate() {
        QueryOption queryOption = new QueryOption("cpt_tab_title");
        queryOption.setTableFilter(new TableFilter("id", 705149394235691008L, EOperateMode.Equals));
        DbCollection coll = dataAccess.query(queryOption);
        assert coll.getData().size() == 1;
        DbEntity oldEntity = coll.getData().get(0);
        oldEntity.put("title_name", "待办" + LocalDateTime.now());
        int rowCount = dataAccess.update(coll);
        System.out.println(rowCount);
    }
}
