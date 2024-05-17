package com.dallxy.user.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AbstractChainContext<T> implements CommandLineRunner, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private final Map<String, List<AbstractChainInterceptor>> chainInterceptors = new HashMap<>();

    public void handler(String type, T requestParam) {
        List<AbstractChainInterceptor> interceptors = chainInterceptors.get(type);
        if (interceptors == null) {
            throw new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.", type));
        }
        interceptors.forEach(interceptor -> {
            if (!interceptor.prehandle(requestParam)) {
                throw new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.", type));
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        applicationContext.getBeansOfType(AbstractChainInterceptor.class).forEach((beanName, bean) -> {
            List<AbstractChainInterceptor> interceptors = chainInterceptors.get(bean.getType());
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(bean);
            chainInterceptors.put(bean.getType(), interceptors);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AbstractChainContext.applicationContext = applicationContext;
    }
}
