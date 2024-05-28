package com.lubase.core.multiDataSource;

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
     * @author zhulz
     */
    public static void setDataSourceCode(String dataSourceCode) {
        dbContextHolder.set(dataSourceCode);
        log.info("已切换到数据源:{}", dataSourceCode);
    }

    /**
     * <p>
     * 获取数据源
     * </p>
     *
     * @return String
     * @author zhulz
     */
    public static String getDataSourceCode() {
        return dbContextHolder.get();
    }

    /**
     * <p>
     * 返回主库
     * </p>
     *
     * @author zhulz
     */
    public static void setMainDataSourceCode() {
        dbContextHolder.remove();
        log.info("进入切换主数据源方法(已经屏蔽)");
    }
}