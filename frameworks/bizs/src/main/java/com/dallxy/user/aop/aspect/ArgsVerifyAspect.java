package com.dallxy.user.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ArgsVerifyAspect {

    @Around("@annotation(com.dallxy.user.aop.annotation.VerifyArgs)")
    public Object verifyArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(Arrays.toString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }


}
