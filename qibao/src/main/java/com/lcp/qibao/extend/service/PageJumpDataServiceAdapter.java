package com.lcp.qibao.extend.service;

import com.lcp.qibao.extend.PageJumpDataService;

public interface PageJumpDataServiceAdapter {
    /**
     * 根据方法id获取服务
     *
     * @param methodId
     * @return
     */
    PageJumpDataService getPageJumpDataService(String methodId);
}
