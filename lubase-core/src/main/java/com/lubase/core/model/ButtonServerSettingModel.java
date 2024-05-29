package com.lubase.core.model;

import lombok.Data;

@Data
public class ButtonServerSettingModel {
    /**
     * 主表代码，删除按钮必须配置此属性
     */
    private String mainTableCode;
    /**
     * 逻辑删除标记
     */
    private Boolean isLogicDelete;

    /**
     * 业务流程id
     */
    private String serviceId;
}
