package com.zakhuang.submitcanceltransaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2016/8/9.
 */
@Aspect
@Configuration
public class CancelableMethodAnnotationAspect {

    @Autowired
    private SctTransactionInterceptor sctTransactionInterceptor;

    @Pointcut(value = "@annotation(com.zakhuang.submitcanceltransaction.annotation.CancelableMethod)")
    public void serviceAnnotation() {
    }

    @Around("serviceAnnotation()")
    public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
        return sctTransactionInterceptor.interceptTransactionMethod(pjp);
    }
}
