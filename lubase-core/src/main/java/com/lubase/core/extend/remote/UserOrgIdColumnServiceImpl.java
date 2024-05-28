package com.lubase.core.extend.remote;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.constant.CacheConst;
import com.lubase.core.extend.IColumnRemoteService;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
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
@Service("userOrgIdColumnServiceImpl")
@Slf4j
public class UserOrgIdColumnServiceImpl implements IColumnRemoteService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DataAccess dataAccess;
    private String urlTemplate;
    @Autowired
    UserOrgIdColumnServiceImpl currentService;

    public UserOrgIdColumnServiceImpl(Environment environment) {
        this.urlTemplate = String.format("%s/userInfo", environment.getProperty("custom.cache-server"));
    }

    @Cacheable(key = "'u:id:'" + "+#key")
    @Override
    public DbEntity getCacheDataByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String url = String.format("%s/getUserInfoById?id=%s", urlTemplate, key);
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
    public String tableKey() {
        return "id";
    }

    @Override
    public String getDescription() {
        return "根据用户id获取组织id";
    }

    @Override
    public String getId() {
        return "850798933700513792";
    }

    @Override
    public String displayCol() {
        return "organization_id";
    }

    @Override
    public DbCollection getAllData() {
        return getDataByTableFilter(null);
    }

    DbCollection getDataByTableFilter(TableFilter filter) {
        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setFixField("organization_id");
        if (filter != null) {
            queryOption.setTableFilter(filter);
        }
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection;
    }
}
