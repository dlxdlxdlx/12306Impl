package com.dallxy.ticketService.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.orderService.dao.entity.OrderDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDaoMapper extends BaseMapper<OrderDao> {
}