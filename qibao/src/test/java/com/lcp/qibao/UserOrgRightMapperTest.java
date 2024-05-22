package com.lcp.qibao;


import com.lcp.core.multiDataSource.ChangeDataSourceService;
import com.lcp.qibao.service.userright.mapper.UserOrgRightMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserOrgRightMapperTest {

    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @Autowired
    UserOrgRightMapper userOrgRightMapper;


    @Test
    void test1() {
        changeDataSourceService.changeDataSourceByTableCode("ss_app");
        userOrgRightMapper.getRoleListByOrgId("1");
    }
}
