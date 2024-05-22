package com.lcp.qibao.exception;

/**
 * invoke service not found
 *
 * @author A
 */
public class InvokeServiceNotFoundException extends Exception {

    private String name, type;

    public InvokeServiceNotFoundException(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getMessage() {
        return String.format("类型：%s，名字：%s 的组件没有找到", this.type, this.name) + super.getMessage();
    }
}
