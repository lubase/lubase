package com.lubase.core.model.customForm;

import com.lubase.orm.QueryOption;
import lombok.Data;

import java.util.List;

/**
 * 表单中的 Group 单元 数据对象
 *
 * @author A
 */
@Data
public class FormGroup {
    private String title;
    /**
     * 值：grid、form
     */
    private String type;
    /**
     * 不管type是什么类型，都需要显示配置字段信息
     */
    private List<DbFieldExtend> fieldList;
    /**
     * 如果是grid类型的话，还需要配置从哪张表获取数据。此表必须是表单主数据所在表的从表
     */
    private String tableCode;
    /**
     * 如果是grid类型的话，还需要配置表格显示的信息
     */
    private String gridInfo;

    /**
     * group的唯一表示，32位， 前端生成
     */
    private  String serialNum;
    /**
     * 检索条件
     */
    private QueryOption queryOption;
}
