package com.lcp.core;

import com.lubase.core.mapper.ProcMSSqlMapper;
import com.lubase.core.multiDataSource.ChangeDataSourceService;
import com.lubase.core.service.DataAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcMSSqlMapperTest {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @Autowired
    ProcMSSqlMapper mapper;

    @Test
    void testProc() {
        changeDataSourceService.changeDataSourceByTableCode("wf_app");
        List<String> list = dataAccess.procGetStringList("proc_getUserProcessIds",
                "688164070687248384");
        System.out.println(list);
    }
}
