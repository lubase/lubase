package com.lubase.core.exception;

/**
 * api 输入参数异常
 *
 * @author A
 */
public class ParameterNotFoundException extends Exception {

    private String paramName;

    public String getParamName() {
        return this.paramName;
    }

    public ParameterNotFoundException(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String getMessage() {
        return String.format("parameter %s not found", this.paramName);
    }
}
