package com.lcp.core.multiDataSource;


import com.lcp.coremodel.DbTable;

public interface ChangeDataSourceService {
    /**
     * 切换到数据表所在的库
     *
     * @param tableCode
     */
    void changeDataSourceByTableCode(String tableCode);

    default void changeDataSourceByTableCode(DbTable table) {
        if (table.isMainDatabase()) {
            if (DBContextHolder.getDataSourceCode() != null) {
                DBContextHolder.setMainDataSourceCode();
            }
        } else {
            if (!table.getDatabaseId().toString().equals(DBContextHolder.getDataSourceCode())) {
                DBContextHolder.setDataSourceCode(table.getDatabaseId().toString());
            }
        }
    }
}
