package com.lcp.wfengine;



import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.service.DataAccess;
import com.lubase.wfengine.auto.entity.WfCallbackEntity;
import com.lubase.wfengine.consumer.UpdateBisTableConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UpdateBisTableConsumerTest {

    @Autowired
    UpdateBisTableConsumer updateBisTableConsumer;

    @Autowired
    DataAccess dataAccess;

    @Test
    void test1() {
        QueryOption queryOption = new QueryOption("wf_callback");
        queryOption.setTableFilter(new TableFilter("id", "1109574895299203072"));
        List<WfCallbackEntity> coll = dataAccess.query(queryOption).getGenericData(WfCallbackEntity.class);
        WfCallbackEntity callbackEntity = coll.get(0);
        updateBisTableConsumer.onMessage(callbackEntity);
    }

}
