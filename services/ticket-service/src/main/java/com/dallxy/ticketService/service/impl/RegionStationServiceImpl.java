package com.dallxy.ticketService.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.common.exception.ClientException;
import com.dallxy.ticketService.dao.RegionDao;
import com.dallxy.ticketService.dao.mapper.RegionDaoMapper;
import com.dallxy.ticketService.dao.mapper.StationDaoMapper;
import com.dallxy.ticketService.dto.req.RegionStationQueryReqDTO;
import com.dallxy.ticketService.dto.resp.RegionStationQueryRespDTO;
import com.dallxy.ticketService.dto.resp.StationQueryRespDTO;
import com.dallxy.ticketService.service.RegionStationService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RegionStationServiceImpl implements RegionStationService {
    private final RegionDaoMapper regionMapper;
    private final RedissonClient redissonClient;
    private final RemoteCache remoteCache;
    private final LocalCache localCache;
    private final StationDaoMapper stationMapper;

    /**
     * 查询车站&城市站点集合信息
     *
     * @param requestParam 车站&站点查询参数
     * @return 车站&站点返回数据集合
     */
    @Override
    public List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam) {
        String key;
        if (StrUtil.isNotBlank(requestParam.getName())) {
            key = REGION_STATION + requestParam.getName();

        }
        key = REGION_STATION + requestParam.getQueryType();
        LambdaQueryWrapper<RegionDao> queryWrapper = switch (requestParam.getQueryType()) {
            case 0 -> Wrappers.lambdaQuery(RegionDao.class)
                    .eq(RegionDao::getPopularFlag, FlagEnum.TRUE.code());
            case 1 -> Wrappers.lambdaQuery(RegionDao.class)
                    .in(RegionDao::getInitial, RegionStationQueryTypeEnum.A_E.getSpells());
            case 2 -> Wrappers.lambdaQuery(RegionDao.class)
                    .in(RegionDao::getInitial, RegionStationQueryTypeEnum.F_J.getSpells());
            case 3 -> Wrappers.lambdaQuery(RegionDao.class)
                    .in(RegionDao::getInitial, RegionStationQueryTypeEnum.K_O.getSpells());
            case 4 -> Wrappers.lambdaQuery(RegionDao.class)
                    .in(RegionDao::getInitial, RegionStationQueryTypeEnum.P_T.getSpells());
            case 5 -> Wrappers.lambdaQuery(RegionDao.class)
                    .in(RegionDao::getInitial, RegionStationQueryTypeEnum.U_Z.getSpells());
            default -> throw new ClientException("查询失败，请检查查询参数是否正确");
        };
        return List.of();
    }

    /**
     * 查询所有车站&城市站点集合信息
     *
     * @return 车站返回数据集合
     */
    @Override
    public List<StationQueryRespDTO> listAllStation() {
        return localCache.get(
                STATION_ALL,
                List.class,
                k -> remoteCache.get(k, () -> stationMapper.selectList(Wrappers.emptyWrapper()), StationQueryRespDTO.class),
                DEFAULT_EXPIRE_DAY,
                TimeUnit.DAYS
        );
    }


}
