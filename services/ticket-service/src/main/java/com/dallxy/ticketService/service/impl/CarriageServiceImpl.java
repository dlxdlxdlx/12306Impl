package com.dallxy.ticketService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.ticketService.dao.CarriageDao;
import com.dallxy.ticketService.dao.mapper.CarriageDaoMapper;
import com.dallxy.ticketService.service.CarriageService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Deprecated
public class CarriageServiceImpl extends ServiceImpl<CarriageDaoMapper, CarriageDao> implements CarriageService {
    private final LocalCache localCache;
    private final RemoteCache remoteCache;
    private final RedissonClient redissonClient;

    @Override
    public List<String> listCarriageNumber(String trainId, Integer carriageType) {
//        final String key = TRAIN_CARRIAGE + trainId;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private String getCarriageNumber(final String key, Integer carriageType) {
        return Optional.ofNullable(remoteCache.getStringRedisTemplate().opsForHash().get(key, String.valueOf(carriageType))).map(Object::toString).orElse(null);
    }


}
