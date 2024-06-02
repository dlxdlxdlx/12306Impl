package com.dallxy.orderService.service;

import com.dallxy.orderService.dto.domain.OrderStatusReversalDTO;
import com.dallxy.orderService.dto.req.CancelTicketOrderReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderCreateReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderPageQueryReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailSelfRespDTO;

/**
 * 订单接口层
 */
public interface OrderService {
    TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn);


    PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam);

    String createTicketOrder(TicketOrderCreateReqDTO requestParam);


    boolean closeTicketOrder(CancelTicketOrderReqDTO requestParam);


    boolean cancelTicketOrder(CancelTicketOrderReqDTO requestParam);


    void statusReversal(OrderStatusReversalDTO requestParam);

    void payCallbackOrder(PayResultCallbackOrderEvent requestParam);

    PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam);

}
