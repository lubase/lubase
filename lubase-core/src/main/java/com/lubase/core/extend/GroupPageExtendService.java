package com.lubase.core.extend;

import com.lubase.core.model.NavVO;
import com.lubase.core.util.ClientMacro;

import java.util.List;

/**
 * 页签页面权限控制服务
 */
public interface GroupPageExtendService {
    /**
     * 扩展页面的id
     *
     * @return
     */
    String getPageId();

    /**
     * 扩展功能描述
     *
     * @return
     */
    String getDescription();

    /**
     * 返回子菜单时扩展事件
     *
     * @param pageId
     * @param pageList
     * @return
     */
    List<NavVO> beforeReturnChildPageData(String pageId, List<NavVO> pageList, ClientMacro clientMacro);
}
