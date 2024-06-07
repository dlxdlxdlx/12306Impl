package com.dallxy.user.aop.aspect;

import com.dallxy.common.exception.ClientException;
import com.dallxy.user.aop.annotation.Idempotent;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

/**
 * 幂等注解AOP拦截器
 */
@Aspect
@RequiredArgsConstructor
public final class IdempotentAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(idempotent)")
    public Object idempotentHandler(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        Object resultObj;



        return resultObj;
    }
}
