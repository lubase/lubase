package com.lcp.qibao.model;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbTable;
import lombok.Data;

import java.util.List;

/**
 * 自定义表单数据对象
 *
 * @author A
 */
@Data
public class CustomFormVO {
    /**
     * 表单的id
     */
    private String id;
    /**
     * 表单名字
     */
    private String name;
    /**
     * 表单配置
     */
    private String form_config;
    /**
     * 表单的备注信息。可用于表单填报时的提示信息，文本格式
     */
    private String Memo;
    /**
     * 表单数据
     */
    private DbEntity data;

    /**
     * 表单所有的字段信息
     */
    private DbTable tableInfo;

    /**
     * 表单的布局信息。数据层级依次为tab>group>form|grid
     */
    private String layout;

    /**
     * 表单注入的脚本
     */
    private String extendScript;

    /**
     * 表单只读属性
     */
    private Boolean readonly;

    /**
     * 表单规则
     */
    private Object rule;

    private List<FormButtonVO> btns;
}
