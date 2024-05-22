package com.lcp.core.exception;

/**
 * 在更新操作时，值长度、是否为空异常记录
 *
 * @author A
 */
public class FieldValueException extends Exception {
    private Boolean valueNotNull;
    private String code, name;
    private Integer expectLength, actualLength;

    /**
     * 值不能为空
     *
     * @param code
     * @param name
     */
    public FieldValueException(String code, String name) {
        valueNotNull = true;
        this.code = code;
        this.name = name;
    }

    /**
     * 值的输入长度超出允许的最大长度
     *
     * @param code
     * @param name
     * @param expectLength
     * @param actualLength
     */
    public FieldValueException(String code, String name, Integer expectLength, Integer actualLength) {
        valueNotNull = false;
        this.code = code;
        this.name = name;
        this.actualLength = actualLength;
        this.expectLength = expectLength;
    }

    @Override
    public String getMessage() {
        if (valueNotNull) {
            return String.format("字段%s(%s)的值不能为空", this.code, this.name);
        } else {
            return String.format("字段%s(%s)的长度为%s，超出最大长度%s", this.code, this.name, this.actualLength, this.expectLength);
        }
    }
}
