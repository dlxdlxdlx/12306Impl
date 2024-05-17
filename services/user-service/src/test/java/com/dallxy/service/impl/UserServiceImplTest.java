package com.dallxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.dallxy.user.aop.annotation.VerifyArgs;
import com.dallxy.user.aop.aspect.ArgsVerifyAspect;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private ArgsVerifyAspect aspect;

    @VerifyArgs(type="test")
    void test(String name){
        System.out.println(name);
    }
    @Test
    void annotationTest(){
       test("test");
    }

    @Data
    class P{
        long id;
        String name;
        String pe;
    }
    @Data
    class D{
        String id;
        String name;
    }
    @Test
    void AutoConvertTest(){
//        hutools 支持自动转换
        P p = new P();
        p.id = 1;
        p.name = "dallxy";
        p.pe = "pe";
        D d = BeanUtil.copyProperties(p, D.class);
        System.out.println(d);
    }
}