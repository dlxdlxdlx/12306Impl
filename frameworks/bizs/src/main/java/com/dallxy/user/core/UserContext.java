package com.dallxy.user.core;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

public final class UserContext {
    private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();


    public static void setUser(UserInfoDTO user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static String getUsername(){
        return Optional.ofNullable(USER_THREAD_LOCAL.get()).map(UserInfoDTO::getUsername).orElse(null);
    }

    public static String getRealName(){
        return Optional.ofNullable(USER_THREAD_LOCAL.get()).map(UserInfoDTO::getRealName).orElse(null);
    }

    public static String getUserId() {
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }

    public static String getToken(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getAccessToken).orElse(null);
    }

    public static void clear(){
        USER_THREAD_LOCAL.remove();
    }
}
