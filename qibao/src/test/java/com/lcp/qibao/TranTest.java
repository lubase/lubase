package com.lcp.qibao;

import com.lcp.core.service.DataAccess;
import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.core.operate.EOperateMode;
import com.lcp.core.TableFilter;
import com.lcp.coremodel.DbEntity;
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
