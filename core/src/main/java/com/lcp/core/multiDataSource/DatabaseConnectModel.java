package com.lcp.core.multiDataSource;

import lombok.Data;

@Data
public class DatabaseConnectModel {
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private String testSql;
    /**
     * 数据库代号
     */
    private String aliasCode;
}
