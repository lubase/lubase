package com.lcp.core;

import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.extend.service.ColumnRemoteServiceAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ColumnRemoteServiceTest {


    @Autowired
    ColumnRemoteServiceAdapter columnRemoteServiceAdapter;

    @Test
    void testLoadInfo() {
        List<IColumnRemoteService> list = columnRemoteServiceAdapter.getAllService();
        assert list.size() > 0;
        for (IColumnRemoteService service : list) {
            String msg = String.format(" id is %s name is %s", service.getId(), service.getDescription());
            System.out.println(msg);
        }

        assert columnRemoteServiceAdapter.getServiceByName("userColumnServiceImpl") != null;
        System.out.println(columnRemoteServiceAdapter.getServiceByName("userColumnServiceImpl").getDescription());
    }
}
