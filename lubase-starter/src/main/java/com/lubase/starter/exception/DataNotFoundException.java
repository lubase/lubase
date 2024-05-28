package com.lubase.starter.exception;

/**
 * 数据未找到或者无权限操作数据
 *
 * @author A
 */
public class DataNotFoundException extends Exception {
    String tableCode;
    String id;

    public DataNotFoundException(String tableCode, Long id) {
        this.tableCode = tableCode;
        this.id = id.toString();
    }

    public DataNotFoundException(String tableCode, String id) {
        this.tableCode = tableCode;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("数据未找到或者无权限操作数据");
    }
}
