package com.lcp.qibao.model;

import com.lcp.coremodel.DbEntity;
import lombok.Data;

/**
 * 表单保存DTO
 *
 * @author A
 */
@Data
public class SaveFormParamDTO {
    /**
     * 功能代码。执行代码寻找路径：功能代码->方法Id->方法Path->执行Component
     * 不可为空
     */
    private String funcCode;

    private String pageId;
    private String formId;

    /**
     * 客户端传递的参数
     */
    private DbEntity data;
}
