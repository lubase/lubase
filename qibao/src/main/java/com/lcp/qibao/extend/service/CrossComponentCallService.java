package com.lcp.qibao.extend.service;

import com.lcp.qibao.extend.CrossComponentService;

/**
 * 跨组织服务调用
 */
public interface CrossComponentCallService {
    CrossComponentService getServiceByIdentification(String id);
}
