package com.lubase.starter.model;

import lombok.Data;

import java.util.HashMap;

/**
 * @author A
 */
@Data
public class InvokeMethodParamDTO {
    /**
     * 按钮的id
     */
    private String funcCode;
    /**
     * 页面ID
     */
    private Long pageId;
    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 方法Id。执行代码寻找路径：方法Id->方法Path->执行Component
     * 此属性可为空
     */
    private Long methodId;

    /**
     * 客户端宏变量
     */
    private String clientMacro;
    /**
     * 客户端传递的参数
     */
    private HashMap<String, String> data;
}
