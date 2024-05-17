package com.dallxy.user.utils;


import com.dallxy.user.core.UserInfoDTO;
import org.junit.Test;

public class JWTUtilsTest {
    @Test
    public void tokenCreateTest(){
        String token = JWTUtils.generateAccessToken(new UserInfoDTO("1", "1", "1", "1"));
        System.out.println(token);
        UserInfoDTO userInfoDTO = JWTUtils.parseJwtToken(token);
        System.out.println(userInfoDTO);
    }
}