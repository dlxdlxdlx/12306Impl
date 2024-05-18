package com.dallxy.database.utils;

public final class MybatisUtils {
    public static boolean insertFailed(int inserted){
        return inserted <= 0;
    }
}
