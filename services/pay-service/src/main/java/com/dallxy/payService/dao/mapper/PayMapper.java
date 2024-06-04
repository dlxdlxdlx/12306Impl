package com.dallxy.payService.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.payService.dao.entity.Pay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper extends BaseMapper<Pay> {
}