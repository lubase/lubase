package com.lcp.core.exception;

/**
 * 通用异常信息
 *
 * @author A
 */
public class InvokeCommonException extends Exception {
    String msg;

    public InvokeCommonException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
