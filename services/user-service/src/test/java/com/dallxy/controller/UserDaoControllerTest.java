package com.dallxy.controller;

import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.user.aop.annotation.VerifyArgs;
import com.dallxy.user.aop.aspect.ArgsVerifyAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoControllerTest {
    @Autowired
    private  LocalCache localCache;
    @Autowired
    private  RemoteCache remoteCache;

    @Autowired
    private ApplicationContext applicationContext;

    @VerifyArgs(type="test")
    void t(String param){
        System.out.println(param);
    }

    @Test
    void login() {
//        System.out.println(applicationContext.getBeansOfType(ArgsVerifyAspect.class));
//        System.out.println(applicationContext.getBeansOfType(LocalCache.class));
    }

    @Test
    void checkLogin() {
        localCache.put("test","test");
        localCache.invalidate("test");
        remoteCache.put("test","test",30, TimeUnit.MINUTES);
        remoteCache.invalidate("test");
        System.out.println(localCache.get("test", String.class, k -> remoteCache.getIfPresent("test")));
    }

    @Test
    void logout() {
    }

    @Test
    void hasUsername() {
    }

    @Test
    void register() {
    }

    @Test
    void update() {
    }

    @Test
    void deletion() {
    }
}