package com.lubase.orm.extend.remote;

import com.lubase.orm.QueryOption;
import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@CacheConfig(cacheNames = CacheConst.CACHE_NAME_USER_INFO)
@Service("userInfoByCodeServiceImpl")
@Slf4j
public class UserInfoByCodeServiceImpl implements IColumnRemoteService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DataAccess dataAccess;
    private String urlTemplate;
    @Autowired
    UserInfoByCodeServiceImpl currentService;
    @Autowired
    UserColumnServiceImpl userColumnService;

    public UserInfoByCodeServiceImpl(Environment environment) {
        this.urlTemplate = String.format("%s/userInfo", environment.getProperty("lubase.cache-server"));
    }

    @Override
    public String tableKey() {
        return "user_code";
    }

    @Override
    public String getDescription() {
        return "业务应用根据用户工号获取用户信息";
    }

    @Override
    public String getId() {
        return "850798612559433728";
    }

    @Override
    public String displayCol() {
        return "user_name";
    }

    @Cacheable(key = "'u:code:'" + "+#key")
    @Override
    public DbEntity getCacheDataByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String url = String.format("%s/getUserInfoByCode?userCode=%s", urlTemplate, key);
        try {
            if (StringUtils.isEmpty(restTemplate.getForEntity(url, DbEntity.class).getBody())) {
                return getDefaultEntity(key);
            }
            return currentService.getCacheDataByKey(key);
        } catch (Exception exception) {
            log.error("请求缓存报错" + url, exception);
            return null;
        }
    }

    @Override
    public DbCollection getAllData() {
        return userColumnService.getAllData();
    }

    @Override
    public DbCollection getDataByFilter(QueryOption clientQuery) {
        return userColumnService.getDataByFilter(clientQuery);
    }
}
