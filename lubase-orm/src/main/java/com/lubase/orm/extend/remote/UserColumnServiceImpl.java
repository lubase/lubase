package com.lubase.orm.extend.remote;

import com.lubase.orm.QueryOption;
import com.lubase.orm.constant.CacheConst;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.DbEntityTool;
import com.lubase.orm.util.TableFilterWrapper;
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
@Service("userColumnServiceImpl")
@Slf4j
public class UserColumnServiceImpl implements IColumnRemoteService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DataAccess dataAccess;
    private String urlTemplate;
    @Autowired
    UserColumnServiceImpl currentService;

    public UserColumnServiceImpl(Environment environment) {
        this.urlTemplate = String.format("%s/userInfo", environment.getProperty("lubase.cache-server"));
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
        return "业务应用关联获取用户信息";
    }

    @Override
    public String getId() {
        return "850798252226777088";
    }

    @Override
    public String displayCol() {
        return "user_name";
    }

    @Override
    public DbCollection getAllData() {
        return getDataByFilter(null);
    }

    @Override
    public DbCollection getDataByFilter(QueryOption clientQuery) {
        QueryOption queryOption = new QueryOption("sa_account");
        queryOption.setFixField("user_name,user_code");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        if (clientQuery != null) {
            queryOption.setPageIndex(clientQuery.getPageIndex());
            queryOption.setPageSize(clientQuery.getPageSize());
            if (DbEntityTool.tableFilterIsNotNull(clientQuery.getTableFilter())) {
                filterWrapper.addFilter(clientQuery.getTableFilter());
            }
        }
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection;
        if (clientQuery != null) {
            collection = dataAccess.query(queryOption);
        } else {
            collection = dataAccess.queryAllData(queryOption);
        }
        for (DbEntity entity : collection.getData()) {
            entity.put("user_name", String.format("%s(%s)", entity.get("user_name"), entity.get("user_code")));
        }
        return collection;
    }
}
