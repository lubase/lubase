package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.model.LoginUser;
import com.lubase.core.service.userright.UserRoleAssignService;
import com.lubase.core.service.userright.model.UserRightInfo;
import com.lubase.core.model.NavVO;
import com.lubase.core.service.userright.RoleRightService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.model.ColumnRightModelVO;
import com.lubase.core.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RoleRightServiceTest {

    @Autowired
    UserRightService userRightService;

    @Autowired
    UserInfoService userService;
    @Autowired
    RoleRightService roleRightService;
    @Autowired
    AppHolderService appHolderService;

    @Autowired
    UserRoleAssignService userRoleAssignService;



    @Test
    void testInitUserRight() {
        Long userId = 688163728524316672L;
        UserRightInfo rightInfo = userRightService.getUserRight(userId);
        assert rightInfo.getIsSupperAdministrator();
        assert !rightInfo.getIsAppAdministrator();
    }

    @Test
    void testGetUserRight() {
        Long userId = 688163728524316672L;
        UserRightInfo rightInfo = userRightService.getUserRight(userId);
        assert rightInfo.getIsSupperAdministrator();
        assert rightInfo.getFuncRightList().size() == 21;
    }

    @Test
    void getAdminNavData() {
        LoginUser user = new LoginUser();
        user.setId(688163728524316672L);
        user.setCode("supper");
        user.setName("超级管理员");
        appHolderService.setUser(user);

        List<NavVO> navVOList = userService.getAdminNavData();
        assert navVOList.size() == 21;

        user = new LoginUser();
        user.setId(688164070687248384L);
        user.setCode("supper");
        user.setName("超级管理员");
        appHolderService.setUser(user);

        navVOList = userService.getAdminNavData();
        System.out.println(navVOList);
        assert navVOList.size() == 5;
    }

    @Test
    void testGetUserRoleList() {
        //admin
        assert userRoleAssignService.getUserRoleList(688163728524316672L).size() == 1;
        //admin1
        assert userRoleAssignService.getUserRoleList(688164070687248384L).size() == 5;
    }

    @Test
    void testSetRoleUser() {
        Long userId = 688163728524316672L;
        Long roleId = 698651031558426624L;
        int oldActorCount = userRoleAssignService.getUserRoleList(userId).size();
        assert userRoleAssignService.getUserRoleList(userId).size() == (oldActorCount + 1);

        assert userRoleAssignService.getUserRoleList(userId).size() == oldActorCount;
    }

    @Test
    void testGetPublicRole() {
        Long userId = 696126339488419840L;
        List<Long> roleList = userRoleAssignService.getUserRoleList(userId);
        System.out.println(roleList);
    }

    @Test
    void testGetRoleFuncList() {
        Long roleId = 695525537208078336L;
        assert roleRightService.getRoleFuncList(roleId).size() == 520;
    }

    @Test
    void testGetUserColumnRight() {
        Long userId = 688164070687248384L;// admin1
        UserRightInfo rightInfo = userRightService.getUserRight(userId);
        assert rightInfo.getIsAppAdministrator();
        List<ColumnRightModelVO> columnRightModelVOList = rightInfo.getColRightList();
        System.out.println(JSON.toJSONString(columnRightModelVOList));

    }

    @Test
    void testOrgRight() {
        Long orgId = 678207695009878016L;
        //List<Long> list = userRoleAssignService.getOrgRoleList(orgId);
        //assert list.size() == 1;
        //System.out.println(list);
        List<Long> list;
        Long userid = 709270476408492032L;
        list = userRoleAssignService.getOrgAssignRoleList(userid);
        System.out.println(list);
        assert list.size() == 1;

        list = userRoleAssignService.getUserRoleList(userid);
        System.out.println(list);
        assert list.size() == 6;
    }
}