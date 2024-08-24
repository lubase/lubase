package com.lubase.core.extend.service;

import com.lubase.core.extend.ExportExtendService;

/**
 * 导出扩展服务调度器
 */
public interface ExportExtendAdapter {
    ExportExtendService getServiceByIdentification(String identityCode);
}
