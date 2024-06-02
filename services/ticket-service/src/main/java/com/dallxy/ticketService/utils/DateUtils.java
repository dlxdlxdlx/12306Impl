package com.dallxy.ticketService.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateUtils {
    /**
     * 日期转换为列车行驶开始时间和结束时间
     *
     * @param date    时间
     * @param pattern 日期格式
     * @return 日期格式对应的时间
     */
    public static String convertDateToLocalTime(Date date, String pattern) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(outputFormatter);
    }
}
