package com.dallxy.user.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ArgsVerifyAspect {

    @Before("@annotation(com.dallxy.user.aop.annotation.VerifyArgs)")
    public void verifyArgs(JoinPoint joinPoint) throws Exception {
        System.out.println("hello");
    }


}
