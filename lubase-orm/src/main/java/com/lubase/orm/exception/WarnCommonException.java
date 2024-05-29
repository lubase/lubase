package com.lubase.orm.exception;

public class WarnCommonException extends Exception {
    String msg;

    public WarnCommonException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
