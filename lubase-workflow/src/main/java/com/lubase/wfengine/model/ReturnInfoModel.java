package com.lubase.wfengine.model;

import lombok.Data;

/**
 * 退回流程节点信息
 */
@Data
public class ReturnInfoModel {
    private String taskId;
    private String tInsId;
    private String displayName;
}
