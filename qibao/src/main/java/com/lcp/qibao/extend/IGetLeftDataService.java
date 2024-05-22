package com.lcp.qibao.extend;

import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.util.ClientMacro;

import java.util.List;

/**
 * 如果树形结构区域的数据源不能通过UI进行配置，后台扩展需要通过实现此接口
 *
 * @author A
 */
public interface IGetLeftDataService extends IBaseInvoke {

    /**
     * 方法执行入口
     *
     * @param pageCode    页面代码
     * @param clientMacro 客户端
     * @return
     */
    List<DbEntity> exe(String pageCode, ClientMacro clientMacro);
}
