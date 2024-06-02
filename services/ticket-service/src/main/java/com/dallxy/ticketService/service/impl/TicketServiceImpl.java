package com.dallxy.ticketService.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.ticketService.common.constant.RedisConstant;
import com.dallxy.ticketService.dao.TicketDao;
import com.dallxy.ticketService.dao.TrainDao;
import com.dallxy.ticketService.dao.TrainStationPriceDao;
import com.dallxy.ticketService.dao.TrainStationRelationDao;
import com.dallxy.ticketService.dao.mapper.TicketDaoMapper;
import com.dallxy.ticketService.dao.mapper.TrainDaoMapper;
import com.dallxy.ticketService.dao.mapper.TrainStationPriceDaoMapper;
import com.dallxy.ticketService.dao.mapper.TrainStationRelationDaoMapper;
import com.dallxy.ticketService.dto.domain.SeatClassDTO;
import com.dallxy.ticketService.dto.domain.TicketListDTO;
import com.dallxy.ticketService.dto.req.CancelTicketOrderReqDTO;
import com.dallxy.ticketService.dto.req.PurchaseTicketReqDTO;
import com.dallxy.ticketService.dto.req.RefundTicketReqDTO;
import com.dallxy.ticketService.dto.req.TicketPageQueryReqDTO;
import com.dallxy.ticketService.dto.resp.PayInfoRespDTO;
import com.dallxy.ticketService.dto.resp.RefundTicketRespDTO;
import com.dallxy.ticketService.dto.resp.TicketPageQueryRespDTO;
import com.dallxy.ticketService.dto.resp.TicketPurchaseRespDTO;
import com.dallxy.ticketService.service.TicketService;
import com.dallxy.ticketService.utils.DateUtils;
import com.dallxy.user.filter.AbstractChainContext;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl extends ServiceImpl<TicketDaoMapper, TicketDao> implements TicketService {
    private final LocalCache localCache;
    private final RemoteCache remoteCache;
    private final TicketDaoMapper ticketMapper;
    private final AbstractChainContext<TicketPageQueryReqDTO> ticketQueryReqDTOAbstractChainContext;
    private final AbstractChainContext<PurchaseTicketReqDTO> purchaseTicketReqDTOAbstractChainContext;
    private final AbstractChainContext<RefundTicketReqDTO> refundTicketReqDTOAbstractChainContext;
    private final RedissonClient redissonClient;
    private final TrainStationRelationDaoMapper trainStationRelationDaoMapper;
    private final TrainDaoMapper trainMapper;
    private final TrainStationPriceDaoMapper trainStationPriceDaoMapper;

    /**
     * 根据条件分页查询车票
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    @Override
//    TODO: 验证参数
    public TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam) {
        //项目启动时就已经加载了所有相关缓存
        StringRedisTemplate stringRedisTemplate = remoteCache.getStringRedisTemplate();
        List<Object> stationDetails = localCache.get(RedisConstant.REGION_TRAIN_STATION_MAPPING,
                (k) -> stringRedisTemplate
                        .opsForHash().multiGet(RedisConstant.REGION_TRAIN_STATION_MAPPING,
                                Arrays.asList(requestParam.getFormStation(), requestParam.getToStation())));

        ArrayList<TicketListDTO> seatResults = new ArrayList<>();
        String buildRegionTrainStationHashKey = String.format(RedisConstant.REGION_TRAIN_STATION, stationDetails.get(0).toString(), stationDetails.get(1).toString());
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(buildRegionTrainStationHashKey))) {
            RLock lock = redissonClient.getLock(RedisConstant.LOCK_REGION_TRAIN_STATION);
            Map<Object, Object> regionTrainStationAllMap = new HashMap<>();
            lock.lock();
            try {
                if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(buildRegionTrainStationHashKey))) {
                    LambdaQueryWrapper<TrainStationRelationDao> trainStationQueryWrapper = Wrappers.lambdaQuery(TrainStationRelationDao.class)
                            .eq(TrainStationRelationDao::getStartRegion, stationDetails.get(0).toString())
                            .eq(TrainStationRelationDao::getEndRegion, stationDetails.get(1).toString());
                    List<TrainStationRelationDao> trainStationRelationList = trainStationRelationDaoMapper.selectList(trainStationQueryWrapper);
                    trainStationRelationList.forEach(each -> {
                        TrainDao train = localCache.get(RedisConstant.TRAIN_INFO + each.getTrainId(), k ->
                                remoteCache.<TrainDao>get(k, key -> {
                                    TrainDao trainDao = trainMapper.selectById(each.getTrainId());
                                    remoteCache.getStringRedisTemplate().opsForValue().set(RedisConstant.TRAIN_INFO + key, JSON.toJSONString(trainDao));
                                    return trainDao;
                                })
                        );
                        TicketListDTO ticketList = TicketListDTO.builder()
                                .trainId(String.valueOf(train.getId()))
                                .trainNumber(train.getTrainNumber())
                                .departureTime(DateUtils.convertDateToLocalTime(each.getDepartureTime(), "HH:mm"))
                                .arrivalTime(DateUtils.convertDateToLocalTime(each.getArrivalTime(), "HH:mm"))
                                .duration(String.valueOf(DateUtil.betweenMs(each.getDepartureTime(), each.getArrivalTime())))
                                .departure(each.getDeparture())
                                .arrival(each.getArrival())
                                .departureFlag(each.getDepartureFlag())
                                .arrivalFlag(each.getArrivalFlag())
                                .trainType(train.getTrainType())
                                .trainBrand(train.getTrainBrand())
                                .daysArrived((int) DateUtil.betweenDay(each.getDepartureTime(), each.getArrivalTime(), false))
                                .saleStatus(new Date().after(train.getSaleTime()) ? 0 : 1)
                                .saleTime(DateUtils.convertDateToLocalTime(train.getSaleTime(), "MM-dd HH:mm"))

                                .build();

                        if (StrUtil.isNotBlank(train.getTrainTag())) {
                            ticketList.setTrainTags(StrUtil.split(train.getTrainTag(), ","));
                        }
                        seatResults.add(ticketList);
                        regionTrainStationAllMap.put(Joiner.on("_").join(each.getTrainId(), each.getDeparture(), each.getArrival()), JSON.toJSONString(ticketList));
                    });
                    stringRedisTemplate.opsForHash().putAll(buildRegionTrainStationHashKey, regionTrainStationAllMap);
                }
            } finally {
                lock.unlock();
            }
        }
//        针对需要查询的每车次火车设置座位类型以及对应的价格 还有数量
        for (TicketListDTO ticketList : seatResults) {
            String key = Joiner.on("_").join(RedisConstant.TRAIN_STATION_PRICE, ticketList.getTrainId(), ticketList.getDeparture(), ticketList.getArrival(), ticketList.getDepartureTime()),
            String trainStationPriceStr = localCache.get(
                    key,
                    k -> {

                        String res = remoteCache.get(k, key2 -> {
                            LambdaQueryWrapper<TrainStationPriceDao> queryWrapper = Wrappers.lambdaQuery(TrainStationPriceDao.class)
                                    .eq(TrainStationPriceDao::getDeparture, ticketList.getDeparture())
                                    .eq(TrainStationPriceDao::getArrival, ticketList.getArrival())
                                    .eq(TrainStationPriceDao::getTrainId, ticketList.getTrainId());
                            String result = JSON.toJSONString(trainStationPriceDaoMapper.selectList(queryWrapper));
                            remoteCache.getStringRedisTemplate().opsForValue().set(k, result);
                            return result;
                        });
                        localCache.put(k, res);
                        return res;
                    }
            );
            List<TrainStationPriceDao> trainStationPriceDaoList = JSON.parseArray(trainStationPriceStr, TrainStationPriceDao.class);
            ArrayList<SeatClassDTO> seatClassList = new ArrayList<>();
            trainStationPriceDaoList.forEach(e -> {
                String seatType = e.getSeatType().toString();
                String keySuffix = Joiner.on("_").join(e.getTrainId(), e.getDeparture(), e.getArrival());
                Object quantityObj = stringRedisTemplate.opsForHash().get(RedisConstant.TRAIN_STATION_REMAINING_TICKET + keySuffix, seatType);

                Integer quantity = Optional.ofNullable(quantityObj)
                        .map(Object::toString)
                        .map(Integer::parseInt)
                        .orElseGet(() -> {
//                            加载座位数量.
                        });
                seatClassList.add(new SeatClassDTO(e.getSeatType(), quantity, new BigDecimal(e.getPrice()).divide(new BigDecimal(100), 1, RoundingMode.HALF_UP), false));
            });
            ticketList.setSeatClassList(seatClassList);
        }
        return TicketPageQueryRespDTO.builder()
                .trainList(seatResults)
                .departureStationList(buildDepartureStationList(seatResults))
                .arrivalStationList(buildArrivalStationList(seatResults))
                .trainBrandList(buildTrainBrandList(seatResults))
                .seatClassTypeList(buildSeatClassList(seatResults))
                .build();
    }

    /**
     * 根据条件查询车票V2高性能版本
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    @Override
    public TicketPageQueryRespDTO pageListTicketQueryV2(TicketPageQueryReqDTO requestParam) {
//        TODO: 使用注解进行解耦

        return TicketPageQueryRespDTO
                .builder()
//                TODO:
                .build();
    }

    /**
     * Builds a list of distinct departure stations from the provided list of ticket results.
     *
     * @param seatResults The list of ticket results to extract departure stations from.
     * @return A list of distinct departure stations.
     */
    private List<String> buildDepartureStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getDeparture).distinct().collect(Collectors.toList());
    }

    /**
     * Builds a list of distinct arrival stations from the provided list of ticket results.
     *
     * @param seatResults The list of ticket results to extract arrival stations from.
     * @return A list of distinct arrival stations.
     */
    private List<String> buildArrivalStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getArrival).distinct().collect(Collectors.toList());
    }

    /**
     * Builds a list of distinct seat classes from the provided list of ticket results.
     *
     * @param seatResults The list of ticket results to extract seat classes from.
     * @return A list of distinct seat classes.
     */
    private List<Integer> buildSeatClassList(List<TicketListDTO> seatResults) {
        Set<Integer> resultSeatClassList = new HashSet<>();
        for (TicketListDTO each : seatResults) {
            for (SeatClassDTO item : each.getSeatClassList()) {
                resultSeatClassList.add(item.getType());
            }
        }
        return resultSeatClassList.stream().toList();
    }

    /**
     * Builds a list of distinct train brands from the provided list of ticket results.
     *
     * @param seatResults The list of ticket results to extract train brands from.
     * @return A list of distinct train brands.
     */
    private List<Integer> buildTrainBrandList(List<TicketListDTO> seatResults) {
        Set<Integer> trainBrandSet = new HashSet<>();
        for (TicketListDTO each : seatResults) {
            if (StrUtil.isNotBlank(each.getTrainBrand())) {
                trainBrandSet.addAll(StrUtil.split(each.getTrainBrand(), ",").stream().map(Integer::parseInt).toList());
            }
        }
        return trainBrandSet.stream().toList();
    }

    /**
     * 购买车票
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号
     */
    @Override
    public TicketPurchaseRespDTO purchaseTicketsV1(PurchaseTicketReqDTO requestParam) {
        return null;
    }

    /**
     * 购买车票V2高性能版本
     * 购买车票的具体业务逻辑通过责任链+限流算法实现对参数进行校验以及进行限流.
     * 接着通过ticketService.executePurchaseTickets执行具体的购买车票逻辑
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号
     */
    @Override
    public TicketPurchaseRespDTO purchaseTicketsV2(PurchaseTicketReqDTO requestParam) {
        return null;
    }

    /**
     * 执行购买车票
     * 被对应购票版本号接口调用 {@link TicketService#purchaseTicketsV1(PurchaseTicketReqDTO)} and {@link TicketService#purchaseTicketsV2(PurchaseTicketReqDTO)}
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号
     */
    @Override
    public TicketPurchaseRespDTO executePurchaseTickets(PurchaseTicketReqDTO requestParam) {
        return null;
    }

    /**
     * 支付单详情查询
     *
     * @param orderSn 订单号
     * @return 支付单详情
     */
    @Override
    public PayInfoRespDTO getPayInfo(String orderSn) {
        return null;
    }

    /**
     * 取消车票订单
     *
     * @param requestParam 取消车票订单入参
     */
    @Override
    public void cancelTicketOrder(CancelTicketOrderReqDTO requestParam) {

    }

    /**
     * 公共退款接口
     *
     * @param requestParam 退款请求参数
     * @return 退款返回详情
     */
    @Override
    public RefundTicketRespDTO commonTicketRefund(RefundTicketReqDTO requestParam) {
        return null;
    }
}
