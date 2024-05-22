package com.lcp.core.extend.remote;

import com.lcp.core.QueryOption;
import com.lcp.core.constant.CacheConst;
import com.lcp.core.extend.IColumnRemoteService;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.coremodel.DbEntity;
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
        this.urlTemplate = String.format("%s/userInfo", environment.getProperty("custom.cache-server"));
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
