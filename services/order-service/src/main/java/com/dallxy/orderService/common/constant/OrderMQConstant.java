package com.dallxy.orderService.common.constant;

/**
 * 消息队列 - 订单业务常量
 */
public final class OrderMQConstant {
    //订单服务相关业务
    public static final String ORDER_DELAY_CLOSE_TOPIC_KEY = "12306:order-service:delay-close-order-topic";

    public static final String ORDER_DELAY_CLOSE_TAG_KEY = "12306:order-service:delay-close-order-tag";

    public static final String LOCK_ORDER_CANCEL_SN_PREFIX = "12306:lock:order-service:cancel:sn:";
}
