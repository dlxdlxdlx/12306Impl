package com.dallxy.ticketService.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.orderService.dao.entity.OrderItemPassengerDao;
import com.dallxy.ticketService.dao.mapper.OrderItemPassengerDaoMapper;
@Service
public class OrderItemPassengerDaoService extends ServiceImpl<OrderItemPassengerDaoMapper, OrderItemPassengerDao> {

}
