package com.lubase.starter.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 用户客户端传递多条数据。比如：多选删除、多选进行功能操作场景
 *
 * @author A
 */
@Data
public class InvokeMethodParamMoreDataDTO {
    /**
     * 功能代码。执行代码寻找路径：功能代码->方法Id->方法Path->执行Component
     * 不可为空
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
    private List<HashMap<String, String>> dataset;
}
