package com.lcp.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.config.AppDbDruidConfig;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.extend.remote.DeptInfoServiceImpl;
import com.lubase.orm.extend.remote.UserColumnServiceImpl;
import com.lubase.orm.extend.remote.UserInfoByCodeServiceImpl;
import com.lubase.orm.extend.remote.UserOrgIdColumnServiceImpl;
import com.lubase.model.DbEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisTest {

    @Autowired
    DataAccess dataAccess;
    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    AppDbDruidConfig druidConfig;

    @Test
    void testConfig() {
        System.out.println(druidConfig.getDruidValue());
    }

    @Autowired
    UserColumnServiceImpl userColumnService;

    @Autowired
    UserInfoByCodeServiceImpl userInfoByCodeService;

    @Autowired
    UserOrgIdColumnServiceImpl userOrgIdColumnService;

    @Autowired
    DeptInfoServiceImpl deptInfoService;

    @Test
    void testGetUserInfoById() {
        DbEntity entity = userColumnService.getCacheDataByKey("688163728524316672");
        assert entity != null;
        assert entity.get("user_code") != null;
        assert entity.get("user_name") != null;
        assert entity.get("organization_id") != null;
        System.out.println(JSON.toJSONString(entity));

        entity = userInfoByCodeService.getCacheDataByKey("admin");
        assert entity != null;
        assert entity.get("user_code") != null;
        assert entity.get("user_name") != null;
        assert entity.get("organization_id") != null;
        System.out.println(JSON.toJSONString(entity));

        entity = userOrgIdColumnService.getCacheDataByKey("688163728524316672");
        assert entity != null;
        assert entity.get("user_code") != null;
        assert entity.get("user_name") != null;
        assert entity.get("organization_id") != null;
        assert entity.get("organization_id").equals("688157486603046912");
        System.out.println(JSON.toJSONString(entity));
    }

    @Test
    void testGetDeptInfoById() {
        DbEntity entity = deptInfoService.getCacheDataByKey("678207695009878016");
        assert entity != null;
        assert entity.get("org_name") != null;
        System.out.println(JSON.toJSONString(entity));
    }

    @Test
    void testGetUserInfoByCode() {
        DbEntity entity = userColumnService.getCacheDataByKey("688163728524316672");
        assert entity != null;
        assert entity.get("user_code") != null;
        System.out.println(JSON.toJSONString(entity));
    }

    @Test
    void testAdd() {
        redisTemplate.opsForValue().set("test", "test12");

        System.out.println(LocalDateTime.now());
        for (int i = 1; i < 10000; i++) {
            redisTemplate.opsForValue().get("test");
        }
        System.out.println(LocalDateTime.now());
    }

    @Test
    void testUserInfoCache() {
        QueryOption queryOption = new QueryOption("cpm_contact_person");
        queryOption.setFixField("con_code,organization_id");
        queryOption.setTableFilter(new TableFilter("id", "715187085941673984"));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        List<DbEntity> list = collection.getData();
        assert list.size() == 1;
        System.out.println(JSON.toJSONString(list));
        System.out.println(JSON.toJSONString(list.get(0).getRefData()));

    }
}
