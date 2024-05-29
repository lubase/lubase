package com.lubase.core.extend.service;

import com.lubase.core.extend.CrossComponentService;

/**
 * 跨组织服务调用
 */
public interface CrossComponentCallService {
    CrossComponentService getServiceByIdentification(String id);
}
