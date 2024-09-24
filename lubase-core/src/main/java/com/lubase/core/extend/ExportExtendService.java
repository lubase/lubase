package com.lubase.core.extend;

import com.lubase.orm.model.DbCollection;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 导出功能扩展接口
 */
public interface ExportExtendService {
    /**
     * 获取列表标识。一级页面去pageId，子表去serialnum
     *
     * @return
     */
    String getTableIdentityCode();

    /**
     * 获取导出文件名字
     *
     * @param memo
     * @return
     */
    default String getFileName(String memo) {
        return memo + "-" + LocalDateTime.now().toString().substring(0, 10);
    }

    /**
     * 获取导出excel 中sheet名字
     *
     * @param memo
     * @return
     */
    default String getSheetName(String memo) {
        return memo + "-" + LocalDateTime.now().toString().substring(0, 10);
    }

    /**
     * 创建文件前事件，可以为列表增加数据
     *
     * @param collection
     * @return
     */
    default DbCollection beforeBuilderFile(DbCollection collection) {
        return collection;
    }

    /**
     * 是否启用重写导出功能。默认不启用
     *
     * @return
     */
    default boolean enableCustomExport() {
        return false;
    }

    /**
     * 重写导出功能。如果需要重写，请务必设置enableCustomExport为true
     *
     * @param collection
     * @param response
     * @param memo
     */
    default void export(DbCollection collection, HttpServletResponse response, String memo) {
    }
}
