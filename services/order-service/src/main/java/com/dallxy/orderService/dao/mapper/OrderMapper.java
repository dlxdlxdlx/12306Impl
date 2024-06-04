package com.dallxy.orderService.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.orderService.dao.entity.OrderDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderDao> {
}