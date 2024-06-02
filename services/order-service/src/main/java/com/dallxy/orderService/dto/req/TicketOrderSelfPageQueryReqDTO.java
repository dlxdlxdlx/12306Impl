package com.dallxy.orderService.dto.req;

import lombok.Data;

/**
 * 本人车票订单分页查询
 */
@Data
public class TicketOrderSelfPageQueryReqDTO  {
    /*当前页*/
    private Long current = 1L;

    /*每页显示数*/
    private Long size = 10L;
}
