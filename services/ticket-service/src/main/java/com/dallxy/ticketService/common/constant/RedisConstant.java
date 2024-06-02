package com.dallxy.ticketService.common.constant;

public final class RedisConstant {
    public static final String TRAIN_CARRIAGE = "12306:ticket-service:train:carriage";
    public static final String LOCK_QUERY_CARRIAGE_NUMBER_LIST = "12306:ticket-service:lock:query:carriage:number:list:";

    public static final String TRAIN_STATION_RELATION = "12306:ticket-service:trainStation:relation";

    public static final String REGION_TRAIN_STATION_MAPPING = "12306:ticket-service:region_trainStation:mapping:";

    public static final String REGION_TRAIN_STATION = "12306:ticket-service:region_trainStation:%s:%s";

    public static final String TRAIN_STATION_PRICE = "12306:ticket-service:train_station_price:%s_%s_%s";

    public static final String TRAIN_STATION_REMAINING_TICKET = "12306:ticket-service:trainStation:remainingTicket:%s_%s_%s";

    public static final String QUERY_ALL_REGION_LIST = "12306:ticket-service:query:all:region:list";
    public static final String LOCK_REGION_TRAIN_STATION = "12306:ticket-service:lock:region_trainStation_relation";

    public static final String TRAIN_INFO = "12306:common-info:train_info";
}
