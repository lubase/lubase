package com.lubase.core.response;

import lombok.Data;

@Data
public class ResponseData<T> {

    /**
     * 是否成功
     */
    private int success;
    /**
     * 结果状态 ,具体状态码参见ResponseCode
     */
    private String code;


    /**
     * 响应消息
     **/
    private String message;

    /**
     * 响应数据
     **/
    private T data;

    /**
     * 接口请求时间
     **/
    private long timestamp;


    /**
     * 初始化，增加接口请求事件
     */
    public ResponseData(int isSuccess) {
        this.timestamp = System.currentTimeMillis();
        this.success = isSuccess;
    }


    public static <T> ResponseData<T> success() {
        ResponseData<T> resultData = new ResponseData<>(1);
        resultData.setSuccess(1);
        resultData.setCode(ResponseCode.RC1000.getCode());
        resultData.setMessage(ResponseCode.RC1000.getMessage());
        return resultData;
    }


    public static <T> ResponseData<T> success(T data) {
        ResponseData<T> resultData = new ResponseData<>(1);
        resultData.setCode(ResponseCode.RC1000.getCode());
        resultData.setMessage(ResponseCode.RC1000.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResponseData<T> error(String message) {
        ResponseData<T> resultData = new ResponseData<>(0);
        resultData.setCode(ResponseCode.RC999.getCode());
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> ResponseData<T> unAuthorized(String message) {
        ResponseData<T> resultData = new ResponseData<>(0);
        resultData.setCode(ResponseCode.RC401.getCode());
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> ResponseData<T> error(String code, String message) {
        ResponseData<T> resultData = new ResponseData<>(0);
        resultData.setCode(code);
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> ResponseData<T> parameterNotFound(String paramName) {
        ResponseData<T> resultData = new ResponseData<>(0);
        resultData.setCode(ResponseCode.ParameterNotFoundException.getCode());
        resultData.setMessage(String.format("参数%s没有找到", paramName));
        return resultData;
    }
}
