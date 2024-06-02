package com.dallxy.ticketService.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.ticketService.common.constant.RedisConstant;
import com.dallxy.ticketService.dao.StationDao;
import com.dallxy.ticketService.dao.TrainStationRelationDao;
import com.dallxy.ticketService.dao.mapper.StationDaoMapper;
import com.dallxy.ticketService.dao.mapper.TrainStationRelationDaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class CacheLoadAutoConfiguration implements CommandLineRunner {
    private final RemoteCache remoteCache;
    private final LocalCache localCache;
    private final StationDaoMapper stationMapper;
    private final TrainStationRelationDaoMapper trainStationRelationMapper;
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
//        获取映射关系, 预加载可能需要的数据
        if (Boolean.FALSE.equals(remoteCache.getStringRedisTemplate().hasKey(RedisConstant.REGION_TRAIN_STATION_MAPPING))) {
            List<StationDao> stationList = stationMapper.selectList(Wrappers.emptyWrapper());
            remoteCache.getStringRedisTemplate()
                    .opsForHash()
                    .putAll(RedisConstant.REGION_TRAIN_STATION_MAPPING,
                            stationList
                                    .stream()
                                    .collect(Collectors.toMap(StationDao::getCode, v -> v, (v1, v2) -> v1)));
        }
//        if (Boolean.FALSE.equals(remoteCache.getStringRedisTemplate().hasKey(RedisConstant.TRAIN_STATION_RELATION))) {
//            List<TrainStationRelationDao> trainStationRelationList = trainStationRelationMapper.selectList(Wrappers.emptyWrapper());
//            remoteCache.getStringRedisTemplate()
//                    .opsForHash()
//                    .putAll(RedisConstant.TRAIN_STATION_RELATION,
//                            trainStationRelationList
//                                    .stream()
//                                    .collect(Collectors.toMap(TrainStationRelationDao::getId, v -> v, (v1, v2) -> v1)));
//        }


    }
}
