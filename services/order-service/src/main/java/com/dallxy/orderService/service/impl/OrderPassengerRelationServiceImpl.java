package com.dallxy.orderService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.orderService.dao.entity.OrderItemPassengerDao;
import com.dallxy.orderService.dao.mapper.OrderItemPassengerMapper;
import com.dallxy.orderService.service.OrderPassengerRelationService;
import org.springframework.stereotype.Service;

@Service
public class OrderPassengerRelationServiceImpl
        extends ServiceImpl<OrderItemPassengerMapper, OrderItemPassengerDao>
        implements OrderPassengerRelationService {
}
