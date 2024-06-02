package com.dallxy.ticketService.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.ticketService.dao.mapper.OrderItemMapper;
import com.dallxy.orderService.dao.entity.OrderItemDao;
@Service
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItemDao> {

}
