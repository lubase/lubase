package com.lubase.wfengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lubase.model.DbEntity;
import lombok.Data;

/**
 * 处理人处理命令
 */
@Data
public class WFCmdRunModel {
    /**
     * 处理人实例ID，取自wfOIns表，对应id
     */
    @JsonProperty(value = "oInsId")
    private String oInsId;
    /**
     * 流程命令id，取自wfcmd表，对应id
     */
    private String cmdId;
    /**
     * 命令描述信息，此属性通过服务器端进行赋值
     */
    private String cmdIdDesc;
    private String cmdMemo;
    /**
     * 如果下一个处理节点为指定处理人，需要传入此参数
     */
    private String designationUser;
    /**
     * 客户端传递的参数
     */
    private DbEntity data;
}
