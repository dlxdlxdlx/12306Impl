package com.dallxy.ticketService.dto.resp;

import java.util.Date;

public class PayInfoRespDTO {
    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 支付总金额
     */
    private Integer totalAmount;

    /**
     * 支付状态
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date gmtPayment;
}
