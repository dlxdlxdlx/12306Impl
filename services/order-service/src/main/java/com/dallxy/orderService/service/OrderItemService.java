package com.dallxy.orderService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dallxy.orderService.dao.entity.OrderItemDao;
import com.dallxy.orderService.dto.domain.OrderItemStatusReversalDTO;
import com.dallxy.orderService.dto.req.TicketOrderItemQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;
import java.util.List;

public interface OrderItemService extends IService<OrderItemDao> {
    void orderItemStatusReversal(OrderItemStatusReversalDTO requestParam);

    List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam);
}
