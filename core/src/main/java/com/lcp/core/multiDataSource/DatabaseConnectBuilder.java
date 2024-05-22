package com.lcp.core.multiDataSource;

import com.lcp.core.model.auto.DmDatabaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 数据库连接对接构建类
 */
@Component
public class DatabaseConnectBuilder {
    /**
     * 构建数据库连接对象
     *
     * @param dmDatabaseEntity
     * @return
     */
    public DatabaseConnectModel buildConnectModel(DmDatabaseEntity dmDatabaseEntity) {
        return buildConnectModel(dmDatabaseEntity, dmDatabaseEntity.getId().toString());
    }

    private DatabaseConnectModel buildConnectModel(DmDatabaseEntity dmDatabaseEntity, String databaseAlias) {
        DatabaseConnectModel databaseConnectModel = new DatabaseConnectModel();
        String driverClass = getDriveClass(dmDatabaseEntity.getDatabase_type());
        databaseConnectModel.setDriverClassName(driverClass);
        databaseConnectModel.setUserName(dmDatabaseEntity.getUser_name());
        databaseConnectModel.setPassword(dmDatabaseEntity.getPassword());
        databaseConnectModel.setTestSql(dmDatabaseEntity.getTest_sql());
        String url = getDBUrl(dmDatabaseEntity, true);
        databaseConnectModel.setUrl(url);
        databaseConnectModel.setAliasCode(databaseAlias);

        if (StringUtils.isBlank(databaseConnectModel.getTestSql())) {
            databaseConnectModel.setTestSql("select 1");
        }

        return databaseConnectModel;
    }

    private String getDriveClass(String databaseType) {
        String driverClass = "";
        switch (databaseType) {
            case "mysql":
                driverClass = "com.mysql.cj.jdbc.Driver";
                break;
            case "sqlserver":
                driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                break;
            default:
                break;
        }
        return driverClass;
    }

    public String getDBUrl(DmDatabaseEntity dataSource, Boolean boolCreateDB) {
        String databaseType = dataSource.getDatabase_type();
        String url = "";
        switch (databaseType) {
            case "sqlserver"://sqlserver
                url = String.format("jdbc:sqlserver://%s:%d;", dataSource.getHost(), dataSource.getPort());
                if (boolCreateDB) {
                    url = String.format("%s Databasename=%s;", url, dataSource.getDatabase_name());
                }
                if (!StringUtils.isBlank(dataSource.getInstance_name())) {
                    url = String.format("%s instanceName=%s", url, dataSource.getInstance_name());
                }
                break;
            case "mysql"://mysql
                url = String.format("jdbc:mysql://%s:%d", dataSource.getHost(), dataSource.getPort());
                if (boolCreateDB) {
                    url += String.format("/%s", dataSource.getDatabase_name());
                }
                url += String.format("?%s", "allowMultiQueries=true&useSSL=false&serverTimezone=" + System.getProperty("user.timezone"));
                break;
            default:
                break;
        }
        return url;
    }
}
