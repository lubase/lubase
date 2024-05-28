package com.lubase.starter.exception;

/**
 * 无权方法此页面或者按钮
 */
public class NoRightAccessFuncException extends Exception {

    public NoRightAccessFuncException() {

    }

    @Override
    public String getMessage() {
        return String.format("无权访问此功能");
    }
}
