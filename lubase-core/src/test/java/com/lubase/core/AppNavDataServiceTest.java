package com.lubase.core;

import com.lubase.orm.service.DataAccess;
import com.lubase.orm.model.DbCollection;
import com.lubase.core.model.NavVO;
import com.lubase.core.service.AppNavDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AppNavDataServiceTest {

    @Autowired
    AppNavDataService appNavDataService;

    @Autowired
    DataAccess dataAccess;

    @Test
    void testLoadAdminPageData() {
        List<NavVO> navVOList = appNavDataService.getAdminNavData();
        for (int i = 1; i < 100; i++) {
            appNavDataService.getAdminNavData();
        }
        NavVO vo = navVOList.get(0);
        String oldDes = vo.getDes();

        DbCollection collection = dataAccess.queryById("ss_page", vo.getId());
        assert collection.getData().size() == 1;
        String newDes = LocalDateTime.now().toString();
        collection.getData().get(0).put("description", newDes);
        assert dataAccess.update(collection) == 1;
        navVOList = appNavDataService.getAdminNavData();
        System.out.println(navVOList.get(0));
        assert navVOList.get(0).getDes().equals(newDes);
    }
}
