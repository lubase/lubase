package com.lubase.orm.multiDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBContextHolder {
    private final static Logger log = LoggerFactory.getLogger(DBContextHolder.class);
    // 对当前线程的操作,线程安全的
    private static final ThreadLocal<String> dbContextHolder = new ThreadLocal<String>();

    /**
     * <p>
     * 切换数据源
     * </p>
     *
     * @param dataSourceCode 数据源配置的code
     * @author bluesky
     */
    public static void setDataSourceCode(String dataSourceCode) {
        // 主库是“0”
        if ("0".equals(dataSourceCode)) {
            setMainDataSourceCode();
        } else {
            dbContextHolder.set(dataSourceCode);
            log.debug("已切换到数据源:{}", dataSourceCode);
        }
    }

    /**
     * <p>
     * 获取数据源
     * </p>
     *
     * @return String
     * @author bluesky
     */
    public static String getDataSourceCode() {
        return dbContextHolder.get();
    }

    /**
     * <p>
     * 返回主库
     * </p>
     *
     * @author bluesky
     */
    public static void setMainDataSourceCode() {
        dbContextHolder.remove();
        log.debug("进入切换主数据源方法(已经屏蔽)");
    }
}