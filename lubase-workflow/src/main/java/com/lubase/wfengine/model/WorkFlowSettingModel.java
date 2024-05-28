package com.lubase.wfengine.model;

import lombok.Data;

@Data
public class WorkFlowSettingModel {
    /**
     * 流程服务id
     */
    private String serviceId;
    /**
     * 待办状态
     */
    private Integer processStatus;
}
