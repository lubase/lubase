package com.lubase.wfengine.model;

import lombok.Data;

/**
 * 页面配置中 流程扩展数据
 */
@Data
public class PageDataWFExtendModel {
    /**
     * 流程业务场景id
     */
    private String serviceId;
    /**
     * 处理状态。 0:待办，1：已办
     */
    private Integer processStatus;
}
