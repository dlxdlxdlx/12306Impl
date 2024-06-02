package com.dallxy.user.aop.aspect;

import com.dallxy.user.aop.annotation.VerifyArgs;
import com.dallxy.user.filter.AbstractChainContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ArgsVerifyAspect {
    private final AbstractChainContext abstractChainContext;

    @Before("@annotation(verifyArgs)")
    public void verifyArgs(JoinPoint joinPoint, VerifyArgs verifyArgs) throws Throwable {
        abstractChainContext.handler(verifyArgs.type(), joinPoint.getArgs()[0]);
    }


}
