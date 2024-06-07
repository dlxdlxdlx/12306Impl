package com.dallxy.ticketService.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.ticketService.common.constant.RedisConstant;
import com.dallxy.ticketService.common.enums.VehicleTypeEnum;
import com.dallxy.ticketService.dao.TrainDao;
import com.dallxy.ticketService.dao.mapper.SeatDaoMapper;
import com.dallxy.ticketService.dao.mapper.TrainDaoMapper;
import com.dallxy.ticketService.dto.domain.RouteDTO;
import com.dallxy.ticketService.dto.resp.TrainPurchaseTicketRespDTO;
import com.dallxy.ticketService.service.SeatService;
import com.dallxy.ticketService.service.TrainStationService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SeatServiceImpl implements SeatService {
    private final SeatDaoMapper seatDaoMapper;
    private final LocalCache localCache;
    private final RemoteCache remoteCache;
    private final RedissonClient redissonClient;
    private final TrainDaoMapper trainDaoMapper;
    private final TrainStationService trainStationService;


    /**
     * 获取列车车厢中可用的座位集合
     *
     * @param trainId        列车 ID
     * @param carriageNumber 车厢号
     * @param seatType       座位类型
     * @param departure      出发站
     * @param arrival        到达站
     * @return 可用座位集合
     */
    @Override
    public List<String> listAvailableSeat(String trainId, String carriageNumber, Integer seatType, String departure, String arrival) {
        throw new UnsupportedOperationException("未实现");
    }

    /**
     * 获取列车车厢余票集合
     *
     * @param trainId           列车 ID
     * @param departure         出发站
     * @param arrival           到达站
     * @param trainCarriageList 车厢编号集合
     * @return 车厢余票集合
     */
    @Override
    public List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList) {
        throw new UnsupportedOperationException("未实现");
    }

    /**
     * 查询列车有余票的车厢号集合
     *
     * @param trainId      列车 ID
     * @param carriageType 车厢类型
     * @param departure    出发站
     * @param arrival      到达站
     * @return 车厢号集合
     */
    @Override
    public List<String> listUsableCarriageNumber(String trainId, Integer carriageType, String departure, String arrival) {
        throw new UnsupportedOperationException("未实现");
    }

    /**
     * 锁定选中以及沿途车票状态
     *
     * @param trainId                     列车 ID
     * @param departure                   出发站
     * @param arrival                     到达站
     * @param trainPurchaseTicketRespList 乘车人以及座位信息
     */
    @Override
    public void lockSeat(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketRespList) {
        throw new UnsupportedOperationException("未实现");
    }

    /**
     * 解锁选中以及沿途车票状态
     *
     * @param trainId                    列车 ID
     * @param departure                  出发站
     * @param arrival                    到达站
     * @param trainPurchaseTicketResults 乘车人以及座位信息
     */
    @Override
    public void unlock(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketResults) {
        throw new UnsupportedOperationException("未实现");
    }

    /**
     * 获取列车车厢从出发到结束站点的余票
     *
     * @param trainId   火车ID
     * @param seatType  座位类型
     * @param departure 出发站
     * @param arrival   到达站
     * @return
     */
    @Override
    public Map<String, String> loadSeatMargin(String trainId, String seatType, String departure, String arrival) {
        TrainDao trainDao = localCache.get(RedisConstant.TRAIN_INFO + trainId, (k) ->
                remoteCache.<TrainDao>get(RedisConstant.TRAIN_INFO + trainId, s -> trainDaoMapper.selectById(trainId)));
        Map<String, Map<String, String>> trainStationRemainingTicketMaps = new HashMap<>();
        String keySuffix = Joiner.on("_").join(trainId, departure, arrival);
//        TODO: impl listTrainStationRoute
        List<RouteDTO> routeDTOList = trainStationService.listTrainStationRoute(trainId, trainDao.getStartStation(), trainDao.getEndStation());

        if (CollUtil.isNotEmpty(routeDTOList)) {
            for (RouteDTO route : routeDTOList) {
                List<SeatDaoMapper.SeatRemain> seatRemains = seatDaoMapper.listSeatRemain(trainId, 0, route.getStartStation(), route.getEndStation());
                Map<String, String> trainStationRemainingTicket = seatRemains.stream()
                        .collect(Collectors.toMap(
                                e -> String.valueOf(e.getType()),
                                e -> String.valueOf(e.getMargin())
                        ));
                String actualKeySuffix = Joiner.on("_").join(trainId, route.getStartStation(), route.getEndStation());
                trainStationRemainingTicketMaps.put(RedisConstant.TRAIN_STATION_REMAINING_TICKET + actualKeySuffix, trainStationRemainingTicket);
            }
        } else {
            HashMap<String, String> trainStationRemainingTicket = new HashMap<>();
            VehicleTypeEnum.findSeatTypesByCode(trainDao.getTrainType())
                    .forEach(e -> trainStationRemainingTicket.put(String.valueOf(e), "0"));
            trainStationRemainingTicketMaps.put(RedisConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix, trainStationRemainingTicket);
        }

        return Optional.ofNullable(trainStationRemainingTicketMaps.get(RedisConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix))
                .orElse(new HashMap<>());

    }
}
