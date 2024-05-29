package com.lubase.core.extend.service;

import com.lubase.core.extend.PageJumpDataService;

public interface PageJumpDataServiceAdapter {
    /**
     * 根据方法id获取服务
     *
     * @param methodId
     * @return
     */
    PageJumpDataService getPageJumpDataService(String methodId);
}
