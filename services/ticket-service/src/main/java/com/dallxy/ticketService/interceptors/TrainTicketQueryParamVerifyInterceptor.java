package com.dallxy.ticketService.interceptors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.ticketService.dao.RegionDao;
import com.dallxy.ticketService.dao.StationDao;
import com.dallxy.ticketService.dao.mapper.RegionDaoMapper;
import com.dallxy.ticketService.dao.mapper.StationDaoMapper;
import com.dallxy.ticketService.dto.req.TicketPageQueryReqDTO;
import com.dallxy.user.filter.AbstractChainInterceptor;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dallxy.ticketService.common.constant.RedisConstant.QUERY_ALL_REGION_LIST;

/**
 * 判断站点是否存在
 */
@Component
@RequiredArgsConstructor
public class TrainTicketQueryParamVerifyInterceptor extends AbstractChainInterceptor<TicketPageQueryReqDTO> {
    private final RegionDaoMapper regionMapper;
    private final StationDaoMapper stationMapper;
    private final RedissonClient redissonClient;
    private final LocalCache localCache;
    private final RemoteCache remoteCache;

    @Override
    public boolean handle(TicketPageQueryReqDTO request) {
        if (Boolean.FALSE.equals(remoteCache.getStringRedisTemplate().hasKey(QUERY_ALL_REGION_LIST))) {
            List<RegionDao> regionList = regionMapper.selectList(Wrappers.emptyWrapper());
            List<StationDao> stationList = stationMapper.selectList(Wrappers.emptyWrapper());
            Map<String, String> regionMap = regionList.stream()
                    .collect(Collectors.toMap(RegionDao::getCode, RegionDao::getName));
            regionMap.putAll(stationList.stream()
                    .collect(Collectors.toMap(StationDao::getCode, StationDao::getName, (k1, k2) -> k1)));
            remoteCache.getStringRedisTemplate()
                    .opsForHash()
                    .putAll(QUERY_ALL_REGION_LIST, regionMap);
             return regionMap.containsKey(request.getFormStation())&&regionMap.containsKey(request.getToStation()) ;
        }
        return remoteCache.getStringRedisTemplate().opsForHash().multiGet(QUERY_ALL_REGION_LIST, Arrays.asList(request.getFormStation(),request.getToStation())).size()==2;
    }
}
