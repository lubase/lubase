package com.lubase.core.extend;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.model.DbEntity;
import com.lubase.core.util.ClientMacro;
import com.lubase.orm.service.DataAccess;

/**
 * 页面模板服务
 */
public interface PageTemplateExtendService {
    /**
     * 解析数据
     *
     * @param pageEntity
     * @param queryOption
     * @param clientMacro
     */
    void beforeExecuteQuery(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro);

    /**
     * 返回主列表数据前事件
     *
     * @param pageEntity
     * @param collection
     * @param clientMacro
     */
    void beforeReturnMainData(DbEntity pageEntity, DbCollection collection, ClientMacro clientMacro);

    /**
     * 执行页面模板的查询
     *
     * @param pageEntity
     * @param queryOption
     * @param clientMacro
     * @return
     */
    default DbCollection executePageTemplate(DataAccess dataAccess, DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {
        return dataAccess.query(queryOption);
    }

    /**
     * 设置页面模板代码
     *
     * @return
     */
    String getTemplateCode();
}
