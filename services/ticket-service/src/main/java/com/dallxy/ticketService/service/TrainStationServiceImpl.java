package com.dallxy.ticketService.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dallxy.ticketService.dao.TrainStationDao;
import com.dallxy.ticketService.dao.mapper.TrainStationDaoMapper;
import com.dallxy.ticketService.dto.domain.RouteDTO;
import com.dallxy.ticketService.dto.resp.TrainStationQueryRespDTO;
import com.dallxy.ticketService.utils.StationCalculateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainStationServiceImpl implements TrainStationService {
    private final TrainStationDaoMapper trainStationMapper;


    /**
     * 根据列车 ID 查询站点信息
     *
     * @param trainId 列车 ID
     * @return 列车经停站信息
     */
    @Override
    public List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId) {
        LambdaQueryWrapper<TrainStationDao> queryWrapper = Wrappers.lambdaQuery(TrainStationDao.class)
                .eq(TrainStationDao::getTrainId,trainId);
        List<TrainStationDao> trainStationList = trainStationMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(trainStationList, TrainStationQueryRespDTO.class);
    }

    /**
     * 计算列车站点路线关系
     * 获取开始站点和目的站点及中间站点信息
     *
     * @param trainId   列车 ID
     * @param departure 出发站
     * @param arrival   到达站
     * @return 列车站点路线关系信息
     */
    @Override
    public List<RouteDTO> listTrainStationRoute(String trainId, String departure, String arrival) {
        LambdaQueryWrapper<TrainStationDao> queryWrapper = Wrappers.lambdaQuery(TrainStationDao.class)
                .eq(TrainStationDao::getTrainId, trainId)
                .select(TrainStationDao::getDeparture);

        List<TrainStationDao> trainStationList = trainStationMapper.selectList(queryWrapper);
        List<String> trainStationAllList = trainStationList.stream().map(TrainStationDao::getDeparture).collect(Collectors.toList());
        return StationCalculateUtils.throughStation(trainStationAllList,departure,arrival);
    }

    /**
     * 获取需列车站点扣减路线关系
     * 获取开始站点和目的站点、中间站点以及关联站点信息
     *
     * @param trainId   列车 ID
     * @param departure 出发站
     * @param arrival   到达站
     * @return 需扣减列车站点路线关系信息
     */
    @Override
    public List<RouteDTO> listTakeoutTrainStationRoute(String trainId, String departure, String arrival) {
        LambdaQueryWrapper<TrainStationDao> queryWrapper = Wrappers.lambdaQuery(TrainStationDao.class)
                .eq(TrainStationDao::getTrainId, trainId)
                .select(TrainStationDao::getDeparture);
        List<TrainStationDao> trainStationDaoList = trainStationMapper.selectList(queryWrapper);
        List<String> trainStationAllList = trainStationDaoList.stream().map(TrainStationDao::getDeparture).collect(Collectors.toList());
        return StationCalculateUtils.throughStation(trainStationAllList, departure, arrival);
    }
}
