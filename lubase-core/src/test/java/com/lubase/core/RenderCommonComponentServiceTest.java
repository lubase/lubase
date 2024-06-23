package com.lubase.core;

import com.lubase.core.model.SelectUserModel;
import com.lubase.core.model.UserSelectCollection;
import com.lubase.core.service.RenderCommonComponentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RenderCommonComponentServiceTest {
    @Autowired
    RenderCommonComponentService renderCommonComponentService;

    @Test
    void test1() {
        UserSelectCollection list1 = renderCommonComponentService.selectUserList("admin", "", 1, 10, true);
        UserSelectCollection list2 = renderCommonComponentService.selectUserList("admin", "", 1, 10, false);

        System.out.println(list1);

        System.out.println(list2);
    }
}
