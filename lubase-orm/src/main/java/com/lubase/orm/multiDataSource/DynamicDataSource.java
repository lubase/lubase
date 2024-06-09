package com.lubase.orm.multiDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.lubase.orm.config.AppDbDruidConfig;
import com.lubase.orm.constant.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Object, Object> dynamicTargetDataSources;

    @Autowired
    AppDbDruidConfig config2;

    @Override
    protected Object determineCurrentLookupKey() {
        String datasource = DBContextHolder.getDataSourceCode();
        if (!StringUtils.isEmpty(datasource)) {
            Map<Object, Object> dynamicTargetDataSources = this.dynamicTargetDataSources;
            if (dynamicTargetDataSources.containsKey(datasource)) {
                log.info("---当前数据源：" + datasource + "---");
            } else {
                log.info("不存在的数据源：" + datasource);
                return null;
            }
        } else {
            //log.info("---当前数据源：默认数据源---");
        }
        return datasource;
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        this.dynamicTargetDataSources = targetDataSources;
    }

    // 删除数据源
    public boolean delDatasources(String datasourceCode) {
        Map<Object, Object> dynamicTargetDataSources = this.dynamicTargetDataSources;
        if (dynamicTargetDataSources.containsKey(datasourceCode)) {
            Set<DruidDataSource> druidDataSourceInstances = DruidDataSourceStatManager.getDruidDataSourceInstances();
            for (DruidDataSource l : druidDataSourceInstances) {
                if (datasourceCode.equals(l.getName())) {
                    dynamicTargetDataSources.remove(datasourceCode);
                    DruidDataSourceStatManager.removeDataSource(l);
                    setTargetDataSources(dynamicTargetDataSources);// 将map赋值给父类的TargetDataSources
                    super.afterPropertiesSet();// 将TargetDataSources中的连接信息放入resolvedDataSources管理
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * 测试数据源连接是否有效
     * </p>
     *
     * @param driveClass driveClass
     * @param url        url
     * @param username   username
     * @param password   password
     * @return boolean
     * @author bluesky
     */
    public boolean testDatasource(String driveClass, String url, String username, String password) {
        try {
            Class.forName(driveClass);
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean createDataSourceWithCheck(DatabaseConnectModel dataSource) throws Exception {
        String dsCode = dataSource.getAliasCode();
        log.info("正在检查数据源：" + dsCode);
        Map<Object, Object> dynamicTargetDataSources = this.dynamicTargetDataSources;
        Boolean result = false;
        if (dsCode.equals(CommonConst.MAIN_DATABASE_ID)) {
            log.info("主库数据源无需重新创建");
        } else if (dynamicTargetDataSources.containsKey(dsCode)) {
            log.info("数据源" + dsCode + "之前已经创建，准备测试数据源是否正常...");
            DruidDataSource druidDataSource = (DruidDataSource) dynamicTargetDataSources.get(dsCode);
            boolean rightFlag = true;
            Connection connection = null;
            try {
                log.info(dsCode + "数据源当前闲置连接数：" + druidDataSource.getPoolingCount());
                long activeCount = druidDataSource.getActiveCount();
                log.info(dsCode + "数据源当前活动连接数：" + activeCount);
                if (activeCount > 0) {
                    log.info(dsCode + "数据源活跃连接堆栈信息：" + druidDataSource.getActiveConnectionStackTrace());
                }
                log.info("准备获取数据库连接...");
                connection = druidDataSource.getConnection();
                log.info("数据源" + dsCode + "正常");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                rightFlag = false;
                log.info("缓存数据源" + dsCode + "已失效，准备删除...");
                if (delDatasources(dsCode)) {
                    log.info("缓存数据源删除成功");
                } else {
                    log.info("缓存数据源删除失败");
                }
            } finally {
                if (null != connection) {
                    connection.close();
                }
            }
            if (rightFlag) {
                log.info("不需要重新创建数据源");
            } else {
                log.info("准备重新创建数据源...");
                result = createDataSource(dataSource);
                log.info("重新创建数据源完成");
            }
        } else {
            result = createDataSource(dataSource);
        }
        return result;
    }

    //创建数据源
    private Boolean createDataSource(DatabaseConnectModel dataSource) throws Exception {
        String code = dataSource.getAliasCode();
        log.info("准备创建数据源" + code);
        String testSql = dataSource.getTestSql();
        String username = dataSource.getUserName();
        String password = dataSource.getPassword();
        String url = dataSource.getUrl();
        String driveClass = dataSource.getDriverClassName();

        Boolean result = false;
        if (testDatasource(driveClass, url, username, password)) {
            result = this.createDataSource(code, driveClass, url, username, password, testSql);
            if (!result) {
                log.error("数据源" + code + "配置正确，但创建失败");
            }
        } else {
            log.error("数据源配置有错误");
        }
        return result;
    }

    // 创建数据源
    private boolean createDataSource(String code, String driveClass, String url, String username, String password, String testSql) {
        try {
            try {
                Class.forName(driveClass);
                // 相当于连接数据库
                DriverManager.getConnection(url, username, password);
            } catch (Exception e) {

                return false;
            }
            @SuppressWarnings("resource")

            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setName(code);
            druidDataSource.setDriverClassName(driveClass);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);

            Properties properties = new Properties();
            try {
                for (String key : config2.getDruidValue().keySet()) {
                    properties.put(key, config2.getDruidValue().get(key));
                }
            } catch (Exception ex) {
                log.error("创建业务库时读取druid配置文件失败", ex);
            }
            druidDataSource.configFromPropety(properties);

            druidDataSource.init();
            this.dynamicTargetDataSources.put(code, druidDataSource);
            // 将map赋值给父类的TargetDataSources
            setTargetDataSources(this.dynamicTargetDataSources);
            // 将TargetDataSources中的连接信息放入resolvedDataSources管理
            super.afterPropertiesSet();
            log.info(code + "数据源初始化成功");
            return true;
        } catch (Exception e) {
            log.error(e + "");
            return false;
        }
    }

}
 