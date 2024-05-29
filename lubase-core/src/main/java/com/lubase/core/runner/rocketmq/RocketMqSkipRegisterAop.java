package com.lubase.core.runner.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
@Slf4j
public class RocketMqSkipRegisterAop {
    /**
     * registerContainer
     */
    @Around("execution(* org.apache.rocketmq.spring.autoconfigure.ListenerContainerConfiguration.registerContainer(..))")
    public Object init(ProceedingJoinPoint joinPoint) {
        log.info("RocketMQ开启代理，默认注册已跳过");
        /// 不调用原生执行方法，直接返回null，跳过方法调用
        // Object proceed = joinPoint.proceed();
        // return proceed
        return null;
    }
}
