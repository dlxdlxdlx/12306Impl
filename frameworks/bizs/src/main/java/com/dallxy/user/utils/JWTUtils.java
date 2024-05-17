package com.dallxy.user.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.alibaba.fastjson2.JSON;
import com.dallxy.user.core.UserInfoDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dallxy.user.constant.UserConstant.*;


public final class JWTUtils {

    private static final long EXPIRATION = 86400L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ISS = "index12306";
    public static final String SECRET = "SecretKey039245678901232039487623456783092349288901402967890140939827";

    /**
     * 生成用户 Token, payload中包含username user id,以及Real name, 设置过期时间为1天
     *
     * @param userInfo 用户信息
     * @return 用户访问 Token
     */
    public static String generateAccessToken(UserInfoDTO userInfo) {

        String token = JWT.create()
                .setSigner(JWTSignerUtil.hs512(SECRET.getBytes()))
                .setIssuedAt(new Date())
                .setIssuer(ISS)
                .setSubject(JSON.toJSONString(userInfo))
                .setExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .sign();


        return TOKEN_PREFIX + token;
    }

    /**
     * 解析用户 Token
     *
     * @param token 用户访问 Token
     * @return 用户信息
     */
    public static UserInfoDTO parseJwtToken(String token) {
        if(StringUtils.isBlank(token)||!token.startsWith(TOKEN_PREFIX)){
            return null;
        }
        token = token.substring(TOKEN_PREFIX.length());
        try {
            JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs512(SECRET.getBytes())).validateDate();
        } catch (ValidateException e) {
            throw new RuntimeException(e);
        }
        JWT jwt = JWTUtil.parseToken(token);
        return JSON.parseObject(String.valueOf(jwt.getPayload("sub")), UserInfoDTO.class);
    }
}
