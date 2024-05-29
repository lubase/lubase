package com.lubase.core.aop;

import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 * 切面处理类，登录日志记录处理
 * </p>
 * @author zhulz
 */
@Aspect
@Component
@Slf4j
public class LoginAspect {

    @Autowired
     DataAccess dataAccess;
    @Autowired
    AppHolderService appHolderService;
    /**
     * 异常切入点,记录异常日志
     */
    @Pointcut("@annotation(com.lubase.core.aop.LoginLog)")
    public void loginErrorLogPoinCut() {
        //do nothing
    }
    /**
     * 设置操作日志切入点 , 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.lubase.core.aop.LoginLog)")
    public void loginLogPoinCut() {
        //do nothing
    }
    /**
     * 正常返回通知
     *
     * @param joinPoint 切入点
     */
    @AfterReturning(value = "loginLogPoinCut()")
    public void saveLoginLog(JoinPoint joinPoint) {
        saveLoginLog("1",joinPoint);
    }
   private  void  saveLoginLog(String success_tag,JoinPoint joinPoint){
       try {
           // 获取RequestAttributes
           RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
           // 从获取RequestAttributes中获取HttpServletRequest的信息
           HttpServletRequest request = null;
           if (requestAttributes != null) {
               request = (HttpServletRequest) requestAttributes
                       .resolveReference(RequestAttributes.REFERENCE_REQUEST);
           }
           Object[] args = joinPoint.getArgs();
           var collect = (LoginInfoVO)Arrays.stream(args).collect(Collectors.toList()).get(0);
           String uid = collect.getUid();
           DbCollection collectionLoginLog = dataAccess.getEmptyData("sl_login_log");
           // 从切面织入点处通过反射机制获取织入点处的方法
           MethodSignature signature = (MethodSignature) joinPoint.getSignature();
           // 获取切入点所在的方法
           Method method = signature.getMethod();
           // 获取操作
           LoginLog annotationLog = method.getAnnotation(LoginLog.class);
           if (annotationLog != null) {
               DbEntity  loginLogEntity= collectionLoginLog.newEntity();
               loginLogEntity.put("user_code",uid);
               loginLogEntity.put("success_tag",success_tag);
               if (request != null) {
                   // 请求IP
                   loginLogEntity.put("ip",IPUtil.getClientIp(request));
               }
               collectionLoginLog.getData().add(loginLogEntity);
               dataAccess.update(collectionLoginLog);
           }
       }catch (Exception e){
           log.error("拦截用户登录日志，AfterReturning发生异常，异常信息：",e);
       }
   }
    /**
     * 异常 返回通知
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "loginErrorLogPoinCut()", throwing = "e")
    public void saveLoginErrorLog(JoinPoint joinPoint, Throwable e){
        saveLoginLog("0",joinPoint);
    }
}
