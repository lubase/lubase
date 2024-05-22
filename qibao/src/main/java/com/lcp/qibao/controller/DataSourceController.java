package com.lcp.qibao.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.lcp.core.multiDataSource.DBContextHolder;
import com.lcp.core.multiDataSource.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {


    @Autowired
    DynamicDataSource dynamicDataSource;

    @RequestMapping(value = "/getMainDatabase", method = RequestMethod.GET)
    public String getMainDatabaseCode() {
        DruidDataSource druidDataSource = (DruidDataSource) dynamicDataSource.getResolvedDefaultDataSource();
        return druidDataSource.getRawJdbcUrl();
    }

    @RequestMapping(value = "/getAppDatabase", method = RequestMethod.GET)
    public List<String> getAppDatabaseCode() {
        Map<Object, DataSource> target = dynamicDataSource.getResolvedDataSources();
        List<String> list = new ArrayList<>();
        for (Object key : target.keySet()) {
            DruidDataSource druidDataSource = (DruidDataSource) target.get(key);
            list.add(druidDataSource.getRawJdbcUrl());
        }
        return list;
    }

    @RequestMapping(value = "/getCurrentDatabaseKey", method = RequestMethod.GET)
    public String getCurrentDatabaseKey() {
        return DBContextHolder.getDataSourceCode();
    }
}
