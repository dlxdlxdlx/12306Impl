package com.dallxy.ticketService.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.orderService.dao.entity.OrderItemDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDao> {
}