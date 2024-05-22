package com.lcp.qibao;


import com.lcp.core.service.DataAccess;
import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.core.TableFilter;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.EDBEntityState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataAccessUpdateTest {
    @Autowired
    DataAccess dataAccess;

    @Test
    void testDeleteData() {
        Long dataId = 708773055735795712L;
        DbCollection currentData = dataAccess.queryById("cpm_testTL_project_leader", dataId, "id");
        assert currentData.getData().size() == 1;

        QueryOption queryOption = new QueryOption("cpm_contact_person");
        queryOption.setTableFilter(new TableFilter("data_id", dataId.toString()));
        queryOption.setFixField("id");
        DbCollection targetData = dataAccess.queryAllData(queryOption);
        assert targetData.getData().size() == 1;

        DbEntity entity = currentData.getData().get(0);
        entity.setState(EDBEntityState.Deleted);
        dataAccess.update(currentData);

        assert dataAccess.queryById("cpm_testTL_project_leader", dataId).getData().size() == 0;
        assert dataAccess.queryAllData(queryOption).getData().size() == 0;
    }
}
