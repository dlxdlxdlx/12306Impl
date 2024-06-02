package com.dallxy.ticketService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dallxy.ticketService.dao.RegionDao;
import com.dallxy.ticketService.dto.req.RegionStationQueryReqDTO;
import com.dallxy.ticketService.dto.resp.RegionStationQueryRespDTO;
import com.dallxy.ticketService.dto.resp.StationQueryRespDTO;

import java.util.List;

public interface RegionStationService extends IService<RegionDao> {
    /**
     * 查询车站&城市站点集合信息
     *
     * @param requestParam 车站&站点查询参数
     * @return 车站&站点返回数据集合
     */
    List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam);

    /**
     * 查询所有车站&城市站点集合信息
     *
     * @return 车站返回数据集合
     */
    List<StationQueryRespDTO> listAllStation();
}
