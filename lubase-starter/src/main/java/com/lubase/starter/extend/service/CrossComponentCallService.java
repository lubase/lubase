package com.lubase.starter.extend.service;

import com.lubase.starter.extend.CrossComponentService;

/**
 * 跨组织服务调用
 */
public interface CrossComponentCallService {
    CrossComponentService getServiceByIdentification(String id);
}
