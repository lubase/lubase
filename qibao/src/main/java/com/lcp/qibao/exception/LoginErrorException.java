package com.lcp.qibao.exception;

public class LoginErrorException extends Exception {

    private String errorCode;
    private String errorMsg;

    public LoginErrorException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
