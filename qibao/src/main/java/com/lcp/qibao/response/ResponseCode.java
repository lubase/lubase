package com.lcp.qibao.response;

public enum ResponseCode {
    RC401(401, "认证失败"),
    /**
     * 请求处理成功。
     **/
    RC1000(1000, "处理成功"),

    /**
     * 未处理的错误，默认
     **/
    RC999(999, "未处理的错误"),

    ParameterNotFoundException(1001, "参数未找到"),

    WarnCommonException(901, "提示信息");

    /**
     * 自定义状态码
     **/
    private final Integer code;

    private final String message;

    public String getCode() {
        return this.code.toString();
    }

    public String getMessage() {
        return this.message;
    }

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}