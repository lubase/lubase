package com.lcp.qibao.extend;

import com.lcp.core.model.DbCollection;
import com.lcp.qibao.util.ClientMacro;

/**
 * 主列表数据源接口
 *
 * @author A
 */
public interface IGetMainDataService extends IBaseInvoke {
    /**
     * 获取主列表数据源
     *
     * @param pageCode    页面代码
     * @param clientMacro 客户端宏变量
     * @return
     */
    DbCollection exe(String pageCode, ClientMacro clientMacro);
}
