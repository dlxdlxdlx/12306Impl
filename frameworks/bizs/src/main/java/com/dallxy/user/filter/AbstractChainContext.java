package com.dallxy.user.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class AbstractChainContext<T> implements CommandLineRunner, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private final Map<String, List<AbstractChainInterceptor>> chainInterceptors = new HashMap<>();

    public void handler(String type, T requestParam,Class<?> clazz) {
        Optional.ofNullable(chainInterceptors.get(type)).orElseThrow(() -> new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.", type)))
                .forEach(interceptor -> {
                    if (!interceptor.prehandle(clazz.cast(requestParam))) {
                        //TODO: 设置interceptor对应的异常
                        throw new RuntimeException(String.format("[%s] check failed.", type));
                    }
                });
    }

    @Override
    public void run(String... args) throws Exception {
        applicationContext.getBeansOfType(AbstractChainInterceptor.class).forEach((beanName, bean) -> {
            List<AbstractChainInterceptor> interceptors = Optional.ofNullable(chainInterceptors.get(bean.getType())).orElse(new ArrayList<>());
            interceptors.add(bean);
            chainInterceptors.put(bean.getType(), interceptors);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AbstractChainContext.applicationContext = applicationContext;
    }
}
