package com.lubase.starter.extend;

import com.lubase.starter.auto.entity.SsPageEntity;
import com.lubase.starter.model.PageInfoVO;

/**
 * 页面配置信息扩展
 */
public interface PageInfoExtendService {
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
     * 返回页面配置信息前事件l
     * @param pageEntity
     * @param pageInfoVO
     * @return
     */
    PageInfoVO beforeReturnPageInfo(SsPageEntity pageEntity, PageInfoVO pageInfoVO);
}
