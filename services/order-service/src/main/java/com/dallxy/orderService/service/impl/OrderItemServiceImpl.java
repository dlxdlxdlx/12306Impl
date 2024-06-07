package com.dallxy.orderService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.orderService.dao.entity.OrderItemDao;
import com.dallxy.orderService.dao.mapper.OrderItemMapper;
import com.dallxy.orderService.dao.mapper.OrderMapper;
import com.dallxy.orderService.dto.domain.OrderItemStatusReversalDTO;
import com.dallxy.orderService.dto.req.TicketOrderItemQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.dallxy.orderService.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemDao> implements OrderItemService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RedissonClient redissonClient;

    @Override
    public void orderItemStatusReversal(OrderItemStatusReversalDTO requestParam) {

    }

    @Override
    public List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam) {
        return List.of();
    }
}
