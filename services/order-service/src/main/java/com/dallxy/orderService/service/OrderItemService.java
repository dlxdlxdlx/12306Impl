package com.dallxy.orderService.service;

import com.dallxy.orderService.dto.domain.OrderItemStatusReversalDTO;
import com.dallxy.orderService.dto.req.TicketOrderItemQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;

import java.util.List;

public interface OrderItemService {
    void orderItemStatusReversal(OrderItemStatusReversalDTO requestParam);

    List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam);
}
