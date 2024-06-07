package com.dallxy.user.aop.annotation;

import com.dallxy.user.constant.IdempotentTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    String message() default "您操作太快，请稍后再试";

    String resource() default "";
    String operation() default "";
    IdempotentTypeEnum type() default IdempotentTypeEnum.RESTFUL;


}

