package com.lubase.core;

import com.alibaba.fastjson.JSON;
import com.lubase.core.model.UserInfoModel;
import com.lubase.core.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserInfoServiceTest {

    @Autowired
    UserInfoService userInfoService;

    @Test
    void test() {
        UserInfoModel infoModel = userInfoService.getUserInfo("admin1");
        System.out.println(JSON.toJSONString(infoModel));
    }

}
