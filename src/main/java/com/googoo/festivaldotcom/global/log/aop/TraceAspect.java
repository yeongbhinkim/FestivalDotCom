package com.googoo.festivaldotcom.global.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class TraceAspect {

    @Around("@annotation(com.googoo.festivaldotcom.global.log.annotation.Trace)")
    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.info("[trace] args={}", args);

        // 대상 메서드 실행
        Object result = joinPoint.proceed();

        log.info("[trace] return value={}", result);

        return result;
    }
}