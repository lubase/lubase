package com.lcp.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.DataAccess;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.service.WFUserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserProcessServiceTest {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    ChangeDataSourceService changeDataSourceService;
    @Autowired
    WFUserInfoService userInfoService;

    @Test
    void testMapper() {
        changeDataSourceService.changeDataSourceByTableCode(WfServiceEntity.TABLE_CODE);
        List<String> ids = dataAccess.procGetStringList("wf_app", "proc_getUserProcessIds", "688164070687248384","", "1");
        assert ids.size() == 1;
        System.out.println(JSON.toJSONString(ids));
    }

    @Test
    void testGetUserInfo() {
        String idOrCode = "admin1";
        OperatorUserModel userModel = userInfoService.getOperatorUserByUserIdOrCode(idOrCode);
        idOrCode = "688164070687248384";
        OperatorUserModel userModel2 = userInfoService.getOperatorUserByUserIdOrCode(idOrCode);
        System.out.println("user1" + JSON.toJSONString(userModel));
        System.out.println("user2" + JSON.toJSONString(userModel2));
    }
}
