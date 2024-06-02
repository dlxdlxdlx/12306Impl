package com.dallxy.ticketService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dallxy.ticketService.dao.CarriageDao;

import java.util.List;

@Deprecated
public interface CarriageService extends IService<CarriageDao> {

    List<String> listCarriageNumber(String trainId, Integer carriageType);
}
