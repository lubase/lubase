package com.lubase.starter.extend.service;

import com.lubase.starter.extend.PageJumpDataService;

public interface PageJumpDataServiceAdapter {
    /**
     * 根据方法id获取服务
     *
     * @param methodId
     * @return
     */
    PageJumpDataService getPageJumpDataService(String methodId);
}
