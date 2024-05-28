package com.lubase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author A
 */
@Data
public class DbField implements Serializable {
    private static final long serialVersionUID = 5584605329383028414L;
    private String id;
    private String tableId;
    @JsonIgnore
    private String tableCode;
    private String code;
    private String name;
    @JsonIgnore
    private String dataType;
    private Integer length;
    private int isNull;
    @JsonIgnore
    private String colDefault;
    private String dataFormat;
    //文本、日期、数值等
    private String eleType;
    //显示格式
    private String eleDisType;
    private Integer isMultivalued;
    /**
     * 值的数量
     */
    private Integer valuesCount;
    @JsonIgnore
    private int visible;
    private String codeTypeId;
    @JsonIgnore
    private String serviceName;
    @JsonIgnore
    private String lookup;
    @JsonIgnore
    private String expression;
    private String groupName;
    private String inputDesc;
    @JsonIgnore
    private int tableFilter2SqlMode;
    private Integer orderId;
    @JsonIgnore
    private Integer rowSpan;
    @JsonIgnore
    private Integer colSpan;
    private Integer uniqueValue;
    private String validateExpression;
    private String validateMsg;
    @JsonIgnore
    private Integer displayWidth;
    @JsonIgnore
    private Boolean needLog;
    /**
     * 渲染标记，false则表示不渲染到表单
     */
    @JsonIgnore
    private Boolean renderFlag;

    /**以上为数据库字段，以下为扩展属性*********************/

    /**
     * 未设置权限模块时，此字段暂时设为4。表示有权限
     */
    @JsonIgnore
    private int right = 4;

    /**
     *
     */
    private int accessRight;

    public int getAccessRight() {
        return Math.min(this.visible, this.right);
    }

    @JsonIgnore
    private EAccessGrade fieldAccess;

    /**
     * 获取字段权限
     *
     * @return
     */
    public EAccessGrade getFieldAccess() {
        return EAccessGrade.fromIndex(Math.min(this.visible, this.right));
    }

    /**
     * 判断是否是主键ID
     *
     * @return
     */
    @JsonIgnore
    public boolean isPrimaryKey() {
        return this.code.toLowerCase().equals("id");
    }
}
