package com.lcp.qibao.controller.handler;

import com.lcp.core.exception.FieldValueException;
import com.lcp.core.exception.ParameterNotFoundException;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.qibao.response.ResponseCode;
import com.lcp.qibao.response.ResponseData;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * controller 层级的错误处理类
 *
 * @author A
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseData<String> defaultError(Exception ex) {
        log.error(ex.getMessage(), ex);
        String msg = ex.getMessage();
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            msg += "\r" + stackTraceElement.toString();
        }
        return ResponseData.error(msg);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseData<String> defaultError(AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseData.unAuthorized(ex.getMessage());
    }

    @ExceptionHandler(value = ParameterNotFoundException.class)
    public ResponseData<String> defaultError(ParameterNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        String errorMsg = String.format("参数[%s]没有传递" +
                "", ex.getParamName());
        return ResponseData.error(ResponseCode.ParameterNotFoundException.getCode(), errorMsg);
    }

    @ExceptionHandler(value = FieldValueException.class)
    public ResponseData<String> defaultError(FieldValueException ex) {
        log.error(ex.getMessage(), ex);
        String errorMsg = ex.getMessage();
        return ResponseData.error(ResponseCode.WarnCommonException.getCode(), errorMsg);
    }

    @ExceptionHandler(value = WarnCommonException.class)
    public ResponseData<String> defaultError(WarnCommonException ex) {
        log.error(ex.getMessage(), ex);
        String errorMsg = ex.getMessage();
        return ResponseData.error(ResponseCode.WarnCommonException.getCode(), errorMsg);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseData<String> defaultError(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage(), ex);
        String errorMsg = String.format("参数[%s]没有传递，类型%s", ex.getParameterName(), ex.getParameterType());
        return ResponseData.error(ResponseCode.ParameterNotFoundException.getCode(), errorMsg);
    }

    @ExceptionHandler(value = SQLServerException.class)
    public ResponseData<String> defaultError(SQLServerException ex) {
        log.error(ex.getMessage(), ex);
        String cause = "sql语句执行异常";
        if (ex.getCause() != null) {
            cause = ex.getCause().getMessage();
        }
        return ResponseData.error("2000", cause);
    }

    @ExceptionHandler(value = SQLException.class)
    public ResponseData<String> defaultError(SQLException ex) {
        log.error(ex.getMessage(), ex);
        String cause = "sql语句执行异常";
        if (ex.getCause() != null) {
            cause = ex.getCause().getMessage();
        }
        return ResponseData.error("2000", cause);
    }

    @ExceptionHandler(value = UncategorizedSQLException.class)
    public ResponseData<String> defaultError(UncategorizedSQLException ex) {
        log.error(ex.getMessage(), ex);
        String cause = "sql语句执行异常";
        if (ex.getCause() != null) {
            cause = ex.getCause().getMessage();
        }
        return ResponseData.error("2000", cause);
    }

    @ExceptionHandler(value = BadSqlGrammarException.class)
    public ResponseData<String> defaultError(BadSqlGrammarException ex) {
        log.error(ex.getMessage(), ex);
        String cause = "sql语句执行异常";
        if (ex.getCause() != null) {
            cause = ex.getCause().getMessage();
        }
        return ResponseData.error("2000", cause);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseData<String> defaultError(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        String cause = "";
        if (ex.getCause() != null) {
            cause = ex.getCause().getMessage();
        }
        String errorMsg = String.format("sql语句执行异常：%s", cause);
        return ResponseData.error("2000", errorMsg);
    }

}
