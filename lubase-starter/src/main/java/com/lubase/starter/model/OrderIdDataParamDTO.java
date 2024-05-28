package com.lubase.starter.model;

import com.lubase.model.DbEntity;
import lombok.Data;

import java.util.List;

/**
 * 保存排序后的数据
 *
 * @author A
 */
@Data
public class OrderIdDataParamDTO {
    /**
     * 功能代码。执行代码寻找路径：功能代码->方法Id->方法Path->执行Component
     * 不可为空
     */
    private String funcCode;
    /**
     * 方法Id。执行代码寻找路径：方法Id->sssortsetting
     * 此属性可为空
     */
    private String methodId;
    /**
     * 客户端传递的参数
     */
    private List<DbEntity> dataset;
}
