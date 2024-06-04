package com.dallxy.orderService.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dallxy.database.dao.PageResponse;
import com.dallxy.orderService.dao.entity.OrderDao;
import com.dallxy.orderService.dao.entity.OrderItemDao;
import com.dallxy.orderService.dao.mapper.OrderItemMapper;
import com.dallxy.orderService.dao.mapper.OrderMapper;
import com.dallxy.orderService.dto.domain.OrderStatusReversalDTO;
import com.dallxy.orderService.dto.req.CancelTicketOrderReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderCreateReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderPageQueryReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailSelfRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.dallxy.orderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RedissonClient redissonClient;

    /**
     * 根据订单号查询车票订单
     *
     * @param orderSn 请求参数
     * @return 订单详情
     */
    @Override
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn) {
        OrderDao orderEntity = orderMapper.selectOne(Wrappers.lambdaQuery(OrderDao.class).eq(OrderDao::getOrderSn, orderSn));
        TicketOrderDetailRespDTO result = BeanUtil.copyProperties(orderEntity, TicketOrderDetailRespDTO.class);
        List<OrderItemDao> orderItemDaoList = orderItemMapper.selectList(Wrappers.lambdaQuery(OrderItemDao.class).eq(OrderItemDao::getOrderSn, orderSn));
        List<TicketOrderPassengerDetailRespDTO> passengerDetails = orderItemDaoList.stream()
                .map(e -> BeanUtil.copyProperties(e, TicketOrderPassengerDetailRespDTO.class))
                .collect(Collectors.toList());
        result.setPassengerDetails(passengerDetails);
        return result;
    }

    /**
     * 根据用户名分页查询车票订单
     *
     * @param requestParam
     * @return
     */
    @Override
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        Page<OrderDao> orderPage = orderMapper.selectPage(new Page(requestParam.getCurrent(), requestParam.getSize()),
                Wrappers.lambdaQuery(OrderDao.class)
                        .eq(OrderDao::getUserId, requestParam.getUserId())
                        .in(OrderDao::getStatus, requestParam.getStatusType())
                        .orderByDesc(OrderDao::getOrderTime));
        return PageResponse.convert(orderPage, e -> {
            TicketOrderDetailRespDTO ticketOrderDetailRespDTO = BeanUtil.copyProperties(e, TicketOrderDetailRespDTO.class);
            LambdaQueryWrapper<OrderItemDao> queryWrapper = Wrappers.lambdaQuery(OrderItemDao.class)
                    .eq(OrderItemDao::getOrderSn, e.getOrderSn());

            List<TicketOrderPassengerDetailRespDTO> list = orderItemMapper.selectList(queryWrapper).stream().map(
                    e -> BeanUtil.copyProperties(e, TicketOrderPassengerDetailRespDTO.class)
            ).collect(Collectors.toList());
            ticketOrderDetailRespDTO.setPassengerDetails(list);

            return ticketOrderDetailRespDTO;
        });
    }

    /**
     * 创建火车票订单
     *
     * @param requestParam
     * @return
     */
    @Override
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam) {
        return "";
    }

    /**
     * 取消火车票订单
     *
     * @param requestParam 请求参数
     * @return
     */
    @Override
    public boolean closeTicketOrder(CancelTicketOrderReqDTO requestParam) {
        return false;
    }

    /**
     * 取消火车票
     *
     * @param requestParam 请求参数
     * @return
     */
    @Override
    public boolean cancelTicketOrder(CancelTicketOrderReqDTO requestParam) {
        return false;
    }

    /**
     * 订单状态翻转
     *
     * @param requestParam 请求参数
     */
    @Override
    public void orderStatusReversal(OrderStatusReversalDTO requestParam) {

    }

    /**
     * 查询本人车票订单
     *
     * @param requestParam 请求参数
     * @return
     */
    @Override
    public PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam) {
        return null;
    }
}
