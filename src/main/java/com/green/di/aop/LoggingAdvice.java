package com.green.di.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component @Aspect
public class LoggingAdvice {
    @Around("execution(* com.green.di.aop.MyMath.*(..))")
    public Object methodCallLog(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        System.out.println("start>> " + pjp.getSignature().getName() + Arrays.toString(pjp.getArgs()));

        Object result = pjp.proceed();

        System.out.println("result = " + result);
        System.out.println("<<end in " + (System.currentTimeMillis() - start) + "ms");

        return result;
    }
}
